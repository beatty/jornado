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
public class JornadoServlet<R extends Request<U>, U extends WebUser> extends HttpServlet {
  private final Router<R> router;
  private final UserService<U> userService;
  private final RequestFactory<U, R> requestFactory;
  private final Config config;
  private final Injector injector;

  @Inject
  @SuppressWarnings("unchecked")
  public JornadoServlet(Router router, UserService userService, RequestFactory requestFactory, Config config, Injector injector) {
    this.router = router;
    this.userService = userService;
    this.requestFactory = requestFactory;
    this.config = config;
    this.injector = injector;
  }

  @Override
  protected void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
    RequestProfile.clear();

    final ServletBackedRequest<U> servletBackedRequest = new ServletBackedRequest<U>(httpServletRequest, userService);
    final R request = requestFactory.createRequest(servletBackedRequest);

    final RouteHandlerData<R> routeHandlerData = router.route(request);

    if (routeHandlerData != null) {
      // TODO: all the user related stuff should move to some sort of filter probably
      final boolean requiresUser = routeHandlerData.getHandlerClass().getAnnotation(RequiresLogin.class) != null;

      Response response = null;
      boolean clearUserCookie = false;

      if (requiresUser && !request.isLoggedIn()) {
        response = new RedirectResponse("/login?target=" + request.getReconstructedUrl());
      } else {
        servletBackedRequest.setRouteHandlerData(routeHandlerData);
        try {
          final Class<? extends Handler<R>> handlerClass = routeHandlerData.getHandlerClass();
          final Handler<R> handler = injector.getInstance(handlerClass);
          response = handler.handle(request);
        } catch (InvalidIdException invalidIdException) {
          clearUserCookie = true;
        }
      }

      respond(httpServletResponse, request, response, clearUserCookie);

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

  private void respond(HttpServletResponse httpServletResponse, R request, Response response, boolean clearUserCookie) throws IOException {
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
      httpServletResponse.setContentType(body.getMediaType().toString());
      renderService.write(httpServletResponse.getWriter(), body);
    }
  }
}