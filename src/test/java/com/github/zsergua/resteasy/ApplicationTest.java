package com.github.zsergua.resteasy;

import com.sun.net.httpserver.HttpServer;
import org.jboss.resteasy.plugins.server.sun.http.HttpContextBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class ApplicationTest {

	@Path("/resources")
	public static class ResourceX {
		@Path("/x")
		@GET
		public String getX() {
			return "ResourceX";
		}
	}

	@Path("/")
	public static class ResourceY {
		@Path("/resources/y")
		@GET
		public String getY() {
			return "ResourceY";
		}
	}

	private HttpContextBuilder contextBuilder;
	private HttpServer httpServer;

	@Before
	public void setup() throws Exception {

		httpServer = HttpServer.create(new InetSocketAddress(8085), 10);

		contextBuilder = new HttpContextBuilder();
		contextBuilder.getDeployment().getScannedResourceClasses().add(ResourceX.class.getName());
		contextBuilder.getDeployment().getScannedResourceClasses().add(ResourceY.class.getName());
		contextBuilder.bind(httpServer);

		httpServer.start();

	}

	@After
	public void end() {
		contextBuilder.cleanup();
		httpServer.stop(0);
	}

	@Test
	public void testScanned() throws Exception {

		URL urlX = new URL("http://localhost:8085/resources/x");
		HttpURLConnection urlConnectionX = (HttpURLConnection) urlX.openConnection();
		urlConnectionX.connect();

		Assert.assertEquals(200, urlConnectionX.getResponseCode());
		Assert.assertEquals("ResourceX", getResponseBody(urlConnectionX));

		URL urlY = new URL("http://localhost:8085/resources/y");
		HttpURLConnection urlConnectionY = (HttpURLConnection) urlY.openConnection();
		urlConnectionY.connect();

		Assert.assertEquals(200, urlConnectionY.getResponseCode());
		Assert.assertEquals("ResourceY", getResponseBody(urlConnectionY));

	}

	private String getResponseBody(HttpURLConnection connection) throws IOException {
		try (InputStream inputStream = connection.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
			return reader.lines().collect(Collectors.joining("\n"));
		}
	}

}
