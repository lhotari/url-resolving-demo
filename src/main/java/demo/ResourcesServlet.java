package demo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The purpose of the example is to demonstrate a use case where a jar file contains some resources that are served from a web application.
 * 
 * This example is for a Servlet 3.0 container. Logging goes to System.out
 * 
 * Let's assume that the jar file has a directory which contains the files to serve (we will use /content) in this example.
 * 
 * Here is an example of the directory structure of such jar file:
 *  .
 *  ├── META-INF
 *  │   └── some_marker_file.xml
 *  └── content
 *      └── hello.txt
 * 
 * The files  are located with a following method:
 * 1. the jar file containing the resources is located by first resolving the URL of the marker file with ClassLoader.getResources("META-INF/some_marker_file.xml")
 * 2. the content base url is for the content directory inside the jar file is resolved by removing the characters after the last '/' in the markerFileUrl and appending "../content/" to that url
 * 3. the URL of the actual content file is resolved with new URL(contentBaseUrl, resourceUri)
 *
 */
@WebServlet("/get")
public class ResourcesServlet extends HttpServlet {
    private static final String CONTENT_BASE_PATH = "../content/";
    private static final long serialVersionUID = 1L;

    public ResourcesServlet() {
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Handling request");
        String resourceUri = request.getParameter("resource");
        if (resourceUri == null) {
            resourceUri = "hello.txt";
        }
        System.out.println("resourceUri:" + resourceUri);
        OutputStream out = response.getOutputStream();
        Enumeration<URL> contentJarMarkerUrls = this.getClass().getClassLoader()
                .getResources("META-INF/some_marker_file.xml");
        while (contentJarMarkerUrls.hasMoreElements()) {
            URL markerFileUrl = contentJarMarkerUrls.nextElement();
            System.out.println("markerFileUrl:" + markerFileUrl);
            String contentBasePath = markerFileUrl.toExternalForm().replaceFirst("/[^/]+$", "/") + CONTENT_BASE_PATH;
            URL contentBaseUrl = new URL(contentBasePath);
            System.out.println("contentBaseUrl:" + contentBaseUrl);
            URL relativeUrl = new URL(contentBaseUrl, resourceUri);
            System.out.println("relativeUrl:" + relativeUrl);
            try {
                InputStream resourceStream = relativeUrl.openStream();
                System.out.println("resource exists.");
                response.setContentType("text/plain");
                response.setCharacterEncoding("ISO-8859-1");
                copyStream(resourceStream, out);
                resourceStream.close();
                break;
            } catch (FileNotFoundException e) {
                // ignore this
                System.out.println("resource doesn't exist.");
            }
        }
        out.close();
    }

    static int copyStream(InputStream in, OutputStream out) throws IOException {
        int byteCount = 0;
        byte[] buffer = new byte[8192];
        int bytesRead = -1;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
            byteCount += bytesRead;
        }
        out.flush();
        return byteCount;
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

    }

}
