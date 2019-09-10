package br.com.ernestoborges.redee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

@SpringBootApplication
public class RedeeApplication extends SpringBootServletInitializer {
	
	private static Class<RedeeApplication> applicationClass = RedeeApplication.class;

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(applicationClass);
	}

	public static void main(String[] args) {
		teste();
		SpringApplication.run(applicationClass, args);
		
	}
	
	
	public static void teste() {
		Client client = ClientBuilder.newClient();
		Entity<String> payload = Entity.text("");
		Response response = client.target("https://irql.bipbop.com.br/?q={q}&apiKey={apiKey}")
		  .request(MediaType.TEXT_PLAIN_TYPE)
		  .post(payload);
		
		
		System.out.println("status: " + response.getStatus());
		System.out.println("headers: " + response.getHeaders());
		System.out.println("body:" + response.readEntity(String.class));
	}
	
	
	
}