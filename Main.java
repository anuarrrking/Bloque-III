package es.uma.rysd.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import es.uma.rysd.entities.Person;
import es.uma.rysd.entities.Specie;
import es.uma.rysd.entities.World;

public class Main {	
	private static Random rand; // para n�meros aleatorios
	private static Scanner sc; // para leer de teclado
	private final static String proxy = "proxy.lcc.uma.es";
	private final static String proxy_port = "3128";
	
    public static void main(String[] args) throws IOException {
    	// Descomente las siguiente l�neas si lo est� probando en el laboratorio y accede a Internet usando el proxy
    	// System.setProperty("https.proxyHost",proxy); 
    	// System.setProperty("https.proxyPort",proxy_port);
    	
        SWClient sw = new SWClient();
        String response = null;
    	rand = new Random();
        sc = new Scanner(System.in);

        do{
        	tallest(sw);
        	whoBornIn1(sw);
			whoBornIn2(sw);
			ageSpecie(sw);
	       	System.out.println("Desea otra ronda (s/n)?");
	       	response = sc.nextLine();
	    }while(response.equals("s"));
        sc.close();
        
    }
    
    // Genera un n�mero entre 0 y max-1 que no haya sido usado previamente (los usados vienen en l)
    public static Integer getRandomResource(int max, List<Integer> l){
    	if(max == l.size()) return null;

    	Integer p = rand.nextInt(max);
    	while(l.contains(p)){
    		p = (p+1)%max;
    	}
    	return p;
    }
    
    // Pregunta que obtiene dos recursos iguales (personajes en este caso) y los compara
    public static void tallest(SWClient sw) throws IOException {
    	// Obteniendo la cantidad de gente almacenada
    	int max_people = sw.getNumberOfResources("people");
    	if(max_people == 0){
    		System.out.println("No se encontraron personas.");
    		return;
    	}
    	
    	System.out.println("Generando nueva pregunta...");
    	// Cogiendo dos personas al azar sin repetir
        List<Integer> used = new ArrayList<Integer>();
    	List<Person> people = new ArrayList<Person>();
    	int counter = 0;
    	while(counter < 2){
    		Integer p = getRandomResource(max_people, used);
    		Person person = sw.getPerson(sw.generateEndpoint("people", p));
    		if(person == null){
    			System.out.println("Hubo un error al encontrar el recurso " + p);
    		} else {
    			people.add(person);
    			counter++;
    		}
    		used.add(p);
    	}
    	
    	// Escribiendo la pregunta y leyendo la opci�n
    	Integer n = null;
    	do{
    		System.out.println("�Qui�n es m�s alto? [0] "+ people.get(0).name + " o [1] " + people.get(1).name);
    		try{
    			n = Integer.parseInt(sc.nextLine());
    		}catch(NumberFormatException ex){
    			n = -1;
    		}
    	}while(n != 0 && n != 1);
    	
    	// Mostrando la informaci�n de los personajes elegidos
    	for(Person p: people){
    		System.out.println(p.name + " mide " + p.height);
    	}
    	
    	// Resultado
    	if(Double.parseDouble(people.get(n).height) >= Double.parseDouble(people.get((n+1)%2).height)){
    		System.out.println("Enhorabuena!!! "+ success[rand.nextInt(success.length)]);
    	} else {
    		System.out.println("Fallaste :( " + error[rand.nextInt(error.length)]);
    	}
    }
    
    // Pregunta que relaciona varios recursos:
    // - Elige un recurso (planeta en este caso)
    // - Pregunta sobre alg�n recurso relacionado (persona que naci� o fue crear ah�)
    // - Busca ese recurso y comprueba si est� relacionado con el primero (si la persona buscada tiene como planeta el original)
    public static void whoBornIn1(SWClient sw) throws IOException {
    	// Obteniendo la cantidad de planetas
    	int max_planet = sw.getNumberOfResources("planets");
    	if(max_planet == 0){
    		System.out.println("No se encontraron planetas.");
    		return;
    	}
    	
    	System.out.println("Generando nueva pregunta...");
    	// Obteniendo el planeta (que tenga personas)
        List<Integer> used = new ArrayList<Integer>();
        World world = null;
        do{
        	Integer p = getRandomResource(max_planet, used);
        	world = sw.getWorld(sw.generateEndpoint("planets", p));
    		if(world == null){
    			System.out.println("Hubo un error al encontrar el recurso " + p);
    		}
        	used.add(p);
        }while(world == null || world.residents == null || world.residents.length < 1);

        // Planteamos la pregunta
        String s = null;
   		System.out.println("�Qui�n naci� o fue creado en " + world.name + "?");
   		s = sc.nextLine();
   		// Buscamos la persona indicada
   		Person p = sw.search(s);
   		
   		// Validamos la respuesta y mostramos sus datos
   		if(p == null){
   			System.out.println("No hay nadie con ese nombre");
   		} else {
   			System.out.println(p.name + " naci� en " + p.homeplanet.name);
   		}
   		
   		// Resultados
   		if(p != null && p.homeplanet.name.equals(world.name)){
    		System.out.println("Enhorabuena!!! "+ success[rand.nextInt(success.length)]);
    	} else {
    		System.out.println("Fallaste :( " + error[rand.nextInt(error.length)]);
    	}
    }
    public static void ageSpecie(SWClient sw) throws IOException{
		int max_specie = sw.getNumberOfResources("species");
		if(max_specie == 0){
			System.out.println("No se encontraron species.");
			return;
		}
		System.out.println("Generando nueva pregunta...");
		//Obtenemos la specie para averiguar su edad promedio
		List<Integer> used = new ArrayList<Integer>();
		Specie specie = null;
		do{
			Integer p = getRandomResource(max_specie, used);
			specie = sw.getSpecie(sw.generateEndpoint("species", p));
			if(specie == null){
				System.out.println("Hubo un error al encontrar el recurso " + p);
			}
			used.add(p);
		}while(specie == null || specie.people == null || specie.people.length < 1);
		// Planteamos la pregunta
		String s = null;
		System.out.println("�Cu�l es la edad promedio de la specie " + specie.name + "?");
		s = sc.nextLine();

		String aux = specie.average_lifespan;

		if(aux == null){
			System.out.println("Error. Valor no v�lido");
		} else {
			System.out.println(specie.name + " tendr� una esperanza de vida de " + aux + " a�os");
		}

		// Resultados
		if(aux != null && specie.average_lifespan.equals(s)){
			System.out.println("Enhorabuena!!! "+ success[rand.nextInt(success.length)]);
		} else {
			System.out.println("Fallaste :( " + error[rand.nextInt(error.length)]);
		}

	}
    // Similar a la previa pero en vez de solicitar que escriba se le ofrecen alternativa para ello:
    // - Se elige una al azar de las disponibles en el recurso, persona del planeta (la correcta)
    // - Se buscar aleatoriamente otras 3 que no est�n relacionados con el recurso (las incorrectas)
    // - Se inserta la correcta de forma aleatoria
    public static void whoBornIn2(SWClient sw) throws IOException {
    	
    	// Obteniendo la cantidad de planetas y personas
    	int max_people = sw.getNumberOfResources("people");
    	int max_planet = sw.getNumberOfResources("planets");
    	if(max_people == 0 || max_planet == 0){
    		System.out.println("No se encontraron personas o planetas.");
    		return;
    	}
    	
    	System.out.println("Generando nueva pregunta...");
    	// Obteniendo el planeta (con personas)
        List<Integer> used = new ArrayList<Integer>();
        World world = null;
        do{
        	Integer p = getRandomResource(max_planet, used);
        	world = sw.getWorld(sw.generateEndpoint("planets", p));
    		if(world == null){
    			System.out.println("Hubo un error al encontrar el recurso " + p);
    		}
        	used.add(p);
        }while(world == null || world.residents == null || world.residents.length < 1);
        used.clear();
        // Cogemos uno al azar como acierto
        String [] residents = world.residents;
        Person correct = sw.getPerson(residents[rand.nextInt(residents.length)]);
        // Metemos todas las personas del planeta como usados para que no se seleccionen
        for(String s: residents){
        	used.add(sw.getIDFromURL(s));
        }
        
        // Buscamos 3 m�s
        List<Person> people = new ArrayList<Person>();
        int contador = 0;
    	while(contador < 3){
    		Integer p = getRandomResource(max_people, used);
    		Person person = sw.getPerson(sw.generateEndpoint("people", p));
    		if(person == null){
    			System.out.println("Hubo un error al encontrar el recurso " + p);
    		} else {
    			people.add(person);
    			contador++;
    		}
    		used.add(p);
    	}
    	// Metemos el correcto de forma aleatoria
    	int pos_correct = rand.nextInt(4);
    	people.add(pos_correct, correct);
    	
    	// Leemos la opci�n
    	Integer n = null;
    	do{
    		System.out.print("�Qui�n naci� o fue fabricado en "+ world.name +"?");
    		for(int i = 0; i < 4; i++){
    			System.out.print(" ["+i+"] "+ people.get(i).name);
    		}
    		System.out.println();
    		try{
    			n = Integer.parseInt(sc.nextLine());
    		}catch(NumberFormatException ex){
    			n = -1;
    		}
    	}while(n < 0 || n > 3);
    	
    	// Se muestran los resultados    	
    	for(Person p: people){
    		System.out.println(p.name + " naci� en " + p.homeplanet.name);
    	}
    	
    	// Resultados
    	if(n == pos_correct){
    		System.out.println("Enhorabuena!!! "+ success[rand.nextInt(success.length)]);
    	} else {
    		System.out.println("Fallaste :( " + error[rand.nextInt(error.length)]);
    	}
    }     
  
	private static String [] success = {"This is the way",
			"Eres uno con la Fuerza. La Fuerza est� contigo",
			"Que la fuerza te acompa�e",
			"Nada ocurre por accidente",
			"Sin duda, maravillosa tu mente es",
			"Cuando te fuiste, no era m�s que el aprendiz. Ahora eres el maestro",
			"La Fuerza te est� llamando, d�jala entrar",
			"Tu cantidad de midiclorianos debe ser muy alta",
			"Una lecci�n aprendida es una lecci�n ganada",
			"Debes creer en ti mismo o nadie lo har�",
			"El camino a la sabiduria es sencillo para aquellos que no se dejan cegar por el ego" };
	private static String [] error = {"El mejor profesor, el fracaso es.",
			"El miedo es el camino hacia el Lado Oscuro. El miedo lleva a la ira, la ira lleva al odio, el odio lleva al sufrimiento. Percibo mucho miedo en ti",
			"Tu carencia de fe resulta molesta",
			"La capacidad de hablar no te hace inteligente",
			"Conc�ntrate en el momento. Siente, no pienses, usa tu instinto",
			"No lo intentes. Hazlo, o no lo hagas, pero no lo intentes",
			"Paciencia, utiliza la Fuerza. Piensa",
			"Siento una perturbaci�n en la fuerza",
			"El lado oscurso se intensifica en ti",
			"El primer paso para corregir un error es la paciencia",
			"El exceso de confianza es el mas peligroso de los descuidos",
			"El camino de la ignorancia es guiado por el miedo" };

}