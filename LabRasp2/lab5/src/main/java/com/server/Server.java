package com.server;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.DAO.CityDAO;
import com.DAO.CountryDAO;
import com.DTO.City;
import com.DTO.Country;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Server {
	private Connection connection;	
	private Session session;
	private MessageProducer producer;
	private MessageConsumer consumer;

	public void start() {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        try {
			connection = factory.createConnection();
	        connection.start();

	        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

	        Destination queueSend = session.createQueue("toClient");
	        Destination queueRec = session.createQueue("fromClient");

	        producer = session.createProducer(queueSend);
	        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
	        consumer = session.createConsumer(queueRec);
	        while(processQuery()) {}
        
        } catch (JMSException e) {
			e.printStackTrace();
		}    
	}
	
	private boolean processQuery() {
		int result = 0;
		String response = "";
		String query = "";
		try {
			Message request = consumer.receive(500);				
			if(request == null)
				return true;
			if (request instanceof TextMessage) {
	            TextMessage textMessage = (TextMessage) request;	            	       	            
	            query = textMessage.getText();
			} else 
				return true;
			
			String[] fields = query.split("#");
			if (fields.length == 0){
				return true;
			} else {
				var action = fields[0];
				Country country;
				City city;
				
				System.out.println(action);
				
				switch(action) {
				case "CountryFindById":
					var id = Long.parseLong(fields[1]);
					country = CountryDAO.findById(id);
					response = country.getName();
					System.out.println(response);
					TextMessage message = session.createTextMessage(response);
					producer.send(message);
					break;
				case "CityFindByCountryid":
					id = Long.parseLong(fields[1]);
					var list = CityDAO.findByCountryId(id);
					var str = new StringBuilder();
					for(City c: list) {
						str.append(c.getId());
						str.append("#");
						str.append(c.getCountryid());
						str.append("#");
						str.append(c.getName());
						str.append("#");
						str.append(c.getPopulation());
						str.append("#");
					}
					response = str.toString();
					System.out.println(response);
					message = session.createTextMessage(response);
					producer.send(message);
					break;
				case "CityFindByName":
					var name = fields[1];
					city = CityDAO.findByName(name);
					response = city.getId().toString()+"#"+city.getCountryid().toString()+"#"+city.getName()+"#"+city.getPopulation().toString();
					System.out.println(response);
					message = session.createTextMessage(response);
					producer.send(message);
					break;
				case "CountryFindByName":
					name = fields[1];
					country = CountryDAO.findByName(name);
					response = country.getId().toString();
					System.out.println(response);
					message = session.createTextMessage(response);
					producer.send(message);
					break;
				case "CityUpdate":
					id = Long.parseLong(fields[1]);
					var countryid = Long.parseLong(fields[2]); 
					name = fields[3];
					var population = Long.parseLong(fields[4]); 
					city = new City(id,countryid,name,population);
					if(CityDAO.update(city))
						response = "true";
					else
						response = "false";
					System.out.println(response);
					message = session.createTextMessage(response);
					producer.send(message);
					break;
				case "CountryUpdate": 
					id = Long.parseLong(fields[1]);
					name = fields[2];
					country = new Country(id,name);
					if (CountryDAO.update(country))
						response = "true";
					else
						response = "false";
					System.out.println(response);
					message = session.createTextMessage(response);
					producer.send(message);
					break;
				case "CityInsert":
					countryid = Long.parseLong(fields[1]); 
					name = fields[2];
					population = Long.parseLong(fields[3]); 
					city = new City((long) 0,countryid,name,population);
					if(CityDAO.insert(city))	
						response = "true";
					else
						response = "false";
					System.out.println(response);
					message = session.createTextMessage(response);
					producer.send(message);
					break;
				case "CountryInsert":
					name = fields[1];
					country = new Country();
					country.setName(name);
					
					System.out.println(name);
					
					if(CountryDAO.insert(country))
						response = "true";
					else
						response = "false";
					System.out.println(response);
					message = session.createTextMessage(response);
					producer.send(message);
					break;
				case "CityDelete":
					id = Long.parseLong(fields[1]);
					city = new City();
					city.setId(id);
					if(CityDAO.delete(city))
						response = "true";
					else
						response = "false";
					System.out.println(response);
					message = session.createTextMessage(response);
					producer.send(message);
					break;
				case "CountryDelete":
					id = Long.parseLong(fields[1]);
					country = new Country();
					country.setId(id);
					if(CountryDAO.delete(country))
						response = "true";
					else
						response = "false";
					System.out.println(response);
					message = session.createTextMessage(response);
					producer.send(message);
					break;
				case "CityAll":
					var list1 = CityDAO.findAll();
					str = new StringBuilder();
					for(City c: list1) {
						str.append(c.getId());
						str.append("#");
						str.append(c.getCountryid());
						str.append("#");
						str.append(c.getName());
						str.append("#");
						str.append(c.getPopulation());
						str.append("#");
					}
					response = str.toString();
					System.out.println(response);
					message = session.createTextMessage(response);
					producer.send(message);
					break;
				case "CountryAll":
					var list2 = CountryDAO.findAll();
					str = new StringBuilder();
					for(Country c: list2) {
						str.append(c.getId());
						str.append("#");
						str.append(c.getName());
						str.append("#");
					}
					response = str.toString();
					System.out.println(response);
					message = session.createTextMessage(response);
					producer.send(message);
					break;
				}
			}
			
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	
	public void close() {
		try {
			session.close();
			connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
        
	}
	
	public static void main(String[] args) {
		var server = new Server();
		server.start();
	}
}
