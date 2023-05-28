package es.uma.rysd.app;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;


import com.google.gson.Gson;

import es.uma.rysd.entities.*;

import javax.net.ssl.HttpsURLConnection;

public class SWClient {
	// TODO: Complete el nombre de la aplicación
	private final String app_name = "";
	private final int year = 2022;

	private final String url_api = "https://swapi.dev/api/";

	// Métodos auxiliares facilitados

	// Obtiene la URL del recurso id del tipo resource
	public String generateEndpoint(String resource, Integer id) {
		return url_api + resource + "/" + id + "/";
	}

	// Dada una URL de un recurso obtiene su ID
	public Integer getIDFromURL(String url) {
		String[] parts = url.split("/");

		return Integer.parseInt(parts[parts.length - 1]);
	}

	// Consulta un recurso y devuelve cuántos elementos tiene
	public int getNumberOfResources(String resource) throws IOException {
		String aux = url_api + resource + "/";
		URL service = new URL(aux);
		HttpsURLConnection connection = (HttpsURLConnection) service.openConnection();
		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestProperty("User-Agent", "My simple application");
		connection.setRequestMethod("GET");

		String auxCode = connection.getResponseCode() + "";
		char c = auxCode.charAt(0);

		if (c != '2') {
			return 0;
		} else {
			Gson pars = new Gson();
			InputStream dentro = connection.getInputStream();
			ResourceCountResult res = pars.fromJson(new InputStreamReader(dentro), ResourceCountResult.class);
			return res.count;
		}

		// TODO: Trate de forma adecuada las posibles excepciones que pueden producirse
		// TODO: Cree la URL correspondiente: https://swapi.dev/api/{recurso}/ reemplazando el recurso por el parámetro
		// TODO: Cree la conexión a partir de la URL
		// TODO: Añada las cabeceras User-Agent y Accept (vea el enunciado)
		// TODO: Indique que es una petición GET
		// TODO: Compruebe que el código recibido en la respuesta es correcto
		// TODO: Deserialice la respuesta a ResourceCountResponse
		// TODO: Obtenga el InputStream de la conexión
		// TODO: Devuelva el número de elementos

	}

	public Person getPerson(String urlname) throws IOException {
		Person p = null;
		URL servicio = null;
		HttpsURLConnection connection;
		urlname = urlname.replaceAll("http:", "https:");
		servicio = new URL(urlname);
		connection = (HttpsURLConnection) servicio.openConnection();
		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestProperty("User-Agent", "My simple application");
		connection.setRequestMethod("GET");

		String auxCode = connection.getResponseCode() + "";
		char c = auxCode.charAt(0);

		if (c == '2') {
			Gson pars = new Gson();
			InputStream dentro = connection.getInputStream();
			p = pars.fromJson(new InputStreamReader(dentro), Person.class);
			p.homeplanet = getWorld(p.homeworld);
		}
		return p;


		// TODO: Trate de forma adecuada las posibles excepciones que pueden producirse
		// TODO: Cree la conexión a partir de la URL recibida
		// TODO: Añada las cabeceras User-Agent y Accept (vea el enunciado)
		// TODO: Indique que es una petición GET
		// TODO: Compruebe que el código recibido en la respuesta es correcto
		// TODO: Deserialice la respuesta a Person
		// TODO: Para las preguntas 2 y 3 (no necesita completar esto para la pregunta 1)
		// TODO: A partir de la URL en el campo homreworld obtenga los datos del planeta y almacénelo en atributo homeplanet
	}

	public World getWorld(String urlname) throws IOException {
		World p = null;
		urlname = urlname.replaceAll("http:", "https:");
		URL servicio = null;
		HttpsURLConnection connection;
		servicio = new URL(urlname);
		connection = (HttpsURLConnection) servicio.openConnection();
		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestProperty("User-Agent", "My simple application");
		connection.setRequestMethod("GET");
		// TODO: Trate de forma adecuada las posibles excepciones que pueden producirse
		// TODO: Cree la conexión a partir de la URL recibida

		// TODO: Añada las cabeceras User-Agent y Accept (vea el enunciado)

		// TODO: Indique que es una petición GET

		// TODO: Compruebe que el código recibido en la respuesta es correcto

		// TODO: Deserialice la respuesta a Planet
		String auxCode = connection.getResponseCode() + "";
		char c = auxCode.charAt(0);

		if (c == '2') {
			Gson pars = new Gson();
			InputStream dentro = connection.getInputStream();
			p = pars.fromJson(new InputStreamReader(dentro), World.class);
		}
		return p;
	}

	public Person search(String name) throws IOException {
		Person p = null;
		URL servicio = null;
		HttpsURLConnection connection;
		String concatAux = URLEncoder.encode(name, "utf-8");
		String auxUrl = url_api + "people/?search=" + concatAux;
		servicio = new URL(auxUrl);
		connection = (HttpsURLConnection) servicio.openConnection();
		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestProperty("User-Agent", "My simple application");
		connection.setRequestMethod("GET");

		String auxCode = connection.getResponseCode() + "";
		char c = auxCode.charAt(0);


		if (c == '2') {
			Gson pars = new Gson();
			InputStream dentro = connection.getInputStream();
			QueryResponse query = pars.fromJson(new InputStreamReader(dentro), QueryResponse.class);
			if (query.results.length != 0) {
				p = query.results[0];
				p.homeplanet = getWorld(p.homeworld);
			}
		}


		return p;
		// TODO: Trate de forma adecuada las posibles excepciones que pueden producirse

		// TODO: Cree la conexión a partir de la URL (url_api + name tratado - vea el enunciado)

		// TODO: Añada las cabeceras User-Agent y Accept (vea el enunciado)

		// TODO: Indique que es una petición GET

		// TODO: Compruebe que el código recibido en la respuesta es correcto

		// TODO: Deserialice la respuesta a SearchResponse -> Use la primera posición del array como resultado

		// TODO: Para las preguntas 2 y 3 (no necesita completar esto para la pregunta 1)
		// TODO: A partir de la URL en el campo homreworld obtenga los datos del planeta y almacénelo en atributo homeplanet

	}

	public Specie getSpecie(String urlname) throws IOException {
		Specie s = null;
		urlname = urlname.replaceAll("http:", "https:");
		URL servicio = null;
		HttpsURLConnection connection;
		servicio = new URL(urlname);
		connection = (HttpsURLConnection) servicio.openConnection();
		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestProperty("User-Agent", "My simple application");
		connection.setRequestMethod("GET");
		// TODO: Trate de forma adecuada las posibles excepciones que pueden producirse
		// TODO: Cree la conexión a partir de la URL recibida

		// TODO: Añada las cabeceras User-Agent y Accept (vea el enunciado)

		// TODO: Indique que es una petición GET

		// TODO: Compruebe que el código recibido en la respuesta es correcto

		// TODO: Deserialice la respuesta a Planet
		String auxCode = connection.getResponseCode() + "";
		char c = auxCode.charAt(0);

		if (c == '2') {
			Gson pars = new Gson();
			InputStream dentro = connection.getInputStream();
			s = pars.fromJson(new InputStreamReader(dentro), Specie.class);

		}
		return s;
	}
/*
	public Specie searchS(String name) throws IOException {
		Specie p = null;
		URL servicio = null;
		HttpsURLConnection connection;
		String concatAux = URLEncoder.encode(name, "utf-8");

			String auxUrl = url_api + "species/?search=" + concatAux;
		servicio = new URL(auxUrl);
		connection = (HttpsURLConnection) servicio.openConnection();
		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestProperty("User-Agent", "My simple application");
		connection.setRequestMethod("GET");

		String auxCode = connection.getResponseCode() + "";
		char c = auxCode.charAt(0);


		if (c == '2') {
			Gson pars = new Gson();
			InputStream dentro = connection.getInputStream();
			QueryResponse query = pars.fromJson(new InputStreamReader(dentro), QueryResponse.class);
			if (query.results.length != 0) {
				p = query.resultados[0];
				p.homeplanet = getSpecie(p.homeworld);
			}
		}
		return p;

	}
*/

}