package com.garaman;

import com.garaman.dao.DAO6;
import com.garaman.servlet.DoSaveAppointment6;
import com.garaman.servlet.ImportServlet6;
import com.garaman.servlet.LoginServlet6;
import com.garaman.servlet.SlotsServlet6;
import com.garaman.servlet.StatSupplierServlet6;
import jakarta.servlet.Servlet;
import org.apache.catalina.Context;
import org.apache.catalina.servlets.DefaultServlet;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

/** Khoi dong Tomcat nhung: phuc vu cac trang HTML (tang view) va cac servlet (tang dieu khien). */
public class Main {
    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(System.getProperty("port", "8080"));
        DAO6.initDatabase();

        File webapp = new File("src/main/webapp");
        if (!webapp.isDirectory()) {
            throw new IllegalStateException("Hay chay tu thu muc goc cua project (khong thay src/main/webapp)");
        }
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir("target/tomcat");
        tomcat.setPort(port);
        tomcat.getConnector();

        Context ctx = tomcat.addContext("", webapp.getAbsolutePath());
        ctx.setSessionTimeout(60);

        // tep tinh: HTML, CSS, JS
        Tomcat.addServlet(ctx, "default", new DefaultServlet());
        ctx.addServletMappingDecoded("/", "default");
        ctx.addWelcomeFile("index.html");
        String[][] mime = {{"html", "text/html;charset=UTF-8"}, {"css", "text/css;charset=UTF-8"},
                {"js", "application/javascript;charset=UTF-8"}, {"json", "application/json"}, {"png", "image/png"},
                {"ico", "image/x-icon"}};
        for (String[] m : mime) ctx.addMimeMapping(m[0], m[1]);

        // cac servlet (tang dieu khien)
        register(ctx, "login", new LoginServlet6(), "/api/login", "/api/logout", "/api/me");
        register(ctx, "slots", new SlotsServlet6(), "/api/slots");
        register(ctx, "doSaveAppointment6", new DoSaveAppointment6(), "/api/appointments");
        register(ctx, "statSupplier", new StatSupplierServlet6(), "/api/stat/suppliers");
        register(ctx, "imports", new ImportServlet6(), "/api/stat/imports", "/api/stat/imports/detail");

        tomcat.start();
        System.out.println("GaraMan dang chay tai http://localhost:" + port + "/");
        tomcat.getServer().await();
    }

    private static void register(Context ctx, String name, Servlet servlet, String... patterns) {
        Tomcat.addServlet(ctx, name, servlet);
        for (String p : patterns) ctx.addServletMappingDecoded(p, name);
    }
}
