package jornado;

import com.google.inject.Inject;
import com.google.inject.Injector;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * TODO: encrypt user cookie with login date and IP address; validate on the way in
 * TODO: Maybe make this compatible with people just using web.xml (e.g. provide crappy no-arg constructor and crappy initializer. ugh.)
 * TODO: support app-specified 500 pages with a nice debug mode
 * TODO: I've gone to some pains to allow an app-specified Request object but didn't close the loop on this
 */
public class JornadoServlet<R extends Request, U extends WebUser> extends HttpServlet {
    private final Router<R> router;
    private final UserService<U> userService;
    private final Config config;
    private final Injector injector;

    @Inject
    @SuppressWarnings("unchecked")
    public JornadoServlet(Router router, UserService userService, Config config, Injector injector) {
        this.router = router;
        this.userService = userService;
        this.config = config;
        this.injector = injector;
    }

    @Override
    protected void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        RequestProfile.clear();

        final ServletBackedRequest<U> request = new ServletBackedRequest<U>(httpServletRequest, userService);

        final RouteHandlerData<R> routeHandlerData = router.route(request);

        if (routeHandlerData != null) {
            // TODO: move this to a filtering mechanism
            final boolean requiresUser = routeHandlerData.getHandlerClass().getAnnotation(RequiresLogin.class) != null;

            Response response = null;
            boolean clearUserCookie = false;
            if (requiresUser && !request.isLoggedIn()) {
                response = new RedirectResponse("/login?target=" + request.getReconstructedUrl());
            } else {
                request.setRouteHandlerData(routeHandlerData);
                try {
                    final Class<? extends Handler<R>> handlerClass = routeHandlerData.getHandlerClass();
                    final Handler<R> handler = injector.getInstance(handlerClass);
                    response = handler.handle((R) request); // TODO
                } catch (InvalidIdException invalidIdException) {
                    clearUserCookie = true;
                }
            }

            if (response == null) {
                throw new RuntimeException("null response");
            }

            final int statusCode = response.getStatus().getCode();
            final String reasonPhrase = response.getStatus().getReasonPhrase();
            if (reasonPhrase == null) {
                httpServletResponse.setStatus(statusCode);
            } else {
                httpServletResponse.sendError(statusCode, reasonPhrase);
            }

            for (HeaderOp op : response.getHeaderOps()) {
                op.execute(httpServletResponse);
            }

            if (clearUserCookie) {
                new ClearCookieOp(request.getCookie(Constants.USER_COOKIE)).execute(httpServletResponse);
            }

            final Body body = response.getBody();
            if (body != null) {
                RenderService renderService = injector.getInstance(body.getRenderServiceClass());

                if (!renderService.isIncremental()) {
                    byte[] buffer = renderService.renderFully(body);
                    httpServletResponse.setContentType(body.getMediaType().toString());
                    httpServletResponse.setContentLength(buffer.length);
                    httpServletResponse.getOutputStream().write(buffer);
                } else {
                    httpServletResponse.setContentType(body.getMediaType().toString());
                    Iterable<byte[]> chunks = renderService.render(body);
                    for (byte[] chunk : chunks) {
                        httpServletResponse.getOutputStream().write(chunk);
                    }
                }
            }

            if (config.isDebug()) {
                RequestProfile.finish();
                PrintWriter writer = new PrintWriter(System.out);
                RequestProfile.render(writer);
                writer.flush();
            }            
        } else {
            // TODO: support app-defined pages
            httpServletResponse.sendError(404);
        }
    }
}