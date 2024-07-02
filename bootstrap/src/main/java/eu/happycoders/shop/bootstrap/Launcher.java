package eu.happycoders.shop.bootstrap;

import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;

public class Launcher {

    private UndertowJaxrsServer server;

    public static void main(String[] args) {
        new Launcher().startOnDefaultPort();
    }

    public void startOnDefaultPort() {
        server = new UndertowJaxrsServer();
        startServer();
    }

    private void startServer() {
        server.start();
        server.deploy(RestEasyUndertowShopApplication.class);
    }
}
