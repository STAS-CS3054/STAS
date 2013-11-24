import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;


// the server for the server-side implementation
public class JettyServer {

	// sets up port boundaries and a default
    private static final int MAX_PORT = 65535;
    private static final int MIN_PORT = 1024;
    private static final int DEFAULT_PORT = 8181;
    static int port;

    // starts the server
    // server starts up, but does not properly display pages or servlet
    public static void main(String[] args) throws Exception {

    	// organises the html pages
        WebAppContext handler1 = new WebAppContext();
        handler1.setContextPath("/servlets");
        handler1.setResourceBase("/servlets");

        // organises servlet
        ServletContextHandler handler2 = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler2.setContextPath("/servlets");
        handler2.addServlet(new ServletHolder(new UserProfileServlet()), "/UserProfileServlet");

        // combines web pages and servlet
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { handler1, handler2 });

        Server server = new Server(getPort(args));

        // finishes server setup and starts the server
        server.setHandler(handlers);
        System.out.println(port);
        System.out.println(server.dump());
        server.start();
        server.join();
    }

    // sets up the server on a free port
    private static int getPort(String[] args) throws Exception {

        port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        if (port < MIN_PORT || port > MAX_PORT)
        	throw new Exception();
        return port;
    }
}
