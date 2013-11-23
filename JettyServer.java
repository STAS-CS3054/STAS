import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

public class JettyServer {

    private static final int MAX_PORT = 65535;
    private static final int MIN_PORT = 1024;
    private static final int DEFAULT_PORT = 8181;
    static int port;

    public static void main(String[] args) throws Exception {

        WebAppContext handler1 = new WebAppContext();
        handler1.setContextPath("/static");
        handler1.setResourceBase("WebRoot/static/");

        ServletContextHandler handler2 = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler2.addServlet(new ServletHolder(new UserProfileServlet()), "/UserProfileServlet");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { handler1, handler2 });

        Server server = new Server(getPort(args));

        server.setHandler(handlers);
        System.out.println(port);
        server.start();
    }

    private static int getPort(String[] args) throws Exception {

        port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        if (port < MIN_PORT || port > MAX_PORT)
        	throw new Exception();
        return port;
    }
}
