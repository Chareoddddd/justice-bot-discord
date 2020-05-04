package Justice.JusticeBot;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.security.auth.login.LoginException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.HierarchyException;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import com.google.common.io.CharStreams;

public class App extends ListenerAdapter
{
	static String prefix = "/";
	String[] letters = {":regional_indicator_a:", ":regional_indicator_b:", ":regional_indicator_c:", ":regional_indicator_d:", ":regional_indicator_e:", ":regional_indicator_f:", ":regional_indicator_g:", ":regional_indicator_h:", ":regional_indicator_i:", ":regional_indicator_j:", ":regional_indicator_k:", ":regional_indicator_l:", ":regional_indicator_m:", ":regional_indicator_n:", ":regional_indicator_o:", ":regional_indicator_p:", ":regional_indicator_q:", ":regional_indicator_r:", ":regional_indicator_s:", ":regional_indicator_t:", ":regional_indicator_u:", ":regional_indicator_v:", ":regional_indicator_w:", ":regional_indicator_x:", ":regional_indicator_y:", ":regional_indicator_z:" };
	String[] lettersUnicode = { "\uD83C\uDDE6", "\uD83C\uDDE7", "\uD83C\uDDE8", "\uD83C\uDDE9", "\uD83C\uDDEA", "\uD83C\uDDEB", "\uD83C\uDDEC", "\uD83C\uDDED", "\uD83C\uDDEE", "\uD83C\uDDEF", "\uD83C\uDDF0", "\uD83C\uDDF1", "\uD83C\uDDF2", "\uD83C\uDDF3", "\uD83C\uDDF4", "\uD83C\uDDF5", "\uD83C\uDDF6", "\uD83C\uDDF7", "\uD83C\uDDF8", "\uD83C\uDDF9", "\uD83C\uDDFA", "\uD83C\uDDFB", "\uD83C\uDDFC", "\uD83C\uDDFD", "\uD83C\uDDFE", "\uD83C\uDDFF" };
	Random rand = new Random();
	
    public static void main( String[] args ) throws LoginException, InterruptedException
    {
    	JDA Bot = new JDABuilder(AccountType.BOT).setToken(System.getenv("BOT_TOKEN")).build();
        Bot.addEventListener(new App());
        Bot.getPresence().setGame(Game.watching(prefix + "help"));
    }
    
    public void onMessageReceived(MessageReceivedEvent e) 
    {
    	try {
		Message msg = e.getMessage();
       		String msgString = msg.getContentRaw();
    		if (msgString.length() >= prefix.length() && msgString.substring(0, prefix.length()).equals(prefix)) {	//on a une commande
			MessageChannel msgChannel = e.getChannel();
			User msgUser = e.getAuthor();
			List<Member> mentionedMembers = msg.getMentionedMembers();
			
			String[] orders;
			if (msgString.contains(Character.toString('"'))) {
				String[] tmp = msgString.split(Character.toString('"'), 0);
				int size = tmp.length / 2 + 1;
				int r = 1;
				orders = new String[size];
				orders[0] = tmp[0];
				for (int i = 1; i < tmp.length; i+=2) {
					orders[r] = tmp[i];
					r++;
				}
			} else {
				orders = msgString.split(" ", 0);
			}
			orders[0] = orders[0].replaceAll(" ", "");
			
    			if (orders[0].equals(prefix + "prefixe")) {
				msg.delete().queue();
    				if (orders.length >= 2)
    					prefixe(orders[1], e, msgChannel);
    			} else if (orders[0].equals(prefix + "poll") && orders.length >= 2 && orders.length <= 27) {
				msg.delete().queue();
    				poll(e, msg, msgChannel, msgUser, mentionedMembers, orders);
    			} else if (orders[0].equals(prefix + "tirage") && orders.length >= 2) {
				msg.delete().queue();
    				tirage(e, msg, msgChannel, msgUser, orders);
    			} else if (orders[0].equals(prefix + "rule34")) {
				msg.delete().queue();
    				rule34(e, msg, msgChannel, msgUser, orders);
    			} else if (orders[0].equals(prefix + "wp")) {
				msg.delete().queue();
    				wp(e, msg, msgChannel, msgUser, orders);
    			} else if (orders[0].equals(prefix + "help")) {
				msg.delete().queue();
    				help(msgChannel);
    			} else {
				
			}
    		}
    	} catch (Exception ex) {
	
    	}
    }
    
    public void help (MessageChannel msgChannel) {
    	
    	EmbedBuilder build = new EmbedBuilder();
		build.setColor(0xaa51e2);
		
		build.setTitle("**Liste des Commandes**");
		build.addField(prefix + "prefixe \"nouveau\"", "Modifie le préfixe", false);
		build.addField(prefix + "poll \"Question\" \"Réponse 1\" \"Réponse 2\" ...", "Effectue un poll avec ou sans réponses données", false);
		build.addField(prefix + "tirage \"Proposition 1\" \"Proposition 2\" ...", "Effectue un tirage au sort parmi les propositions données", false);
		build.addField(prefix + "rule34 \"tag 1\" \"tag 2\" ...", "Recherche une image sur rule34 en utilisant les tags fournis (Canal NSFW seulement)", false);
		build.addField(prefix + "wp \"tag 1\" \"tag 2\" ...", "Recherche une image sur Wallpaper Abyss en utilisant les tags fournis", false);
		msgChannel.sendMessage(build.build()).queue();
	}
    
    public void prefixe(String p, MessageReceivedEvent e, MessageChannel msgChannel) {
    	EmbedBuilder build = new EmbedBuilder();
    	
		String message = "Le prefixe " + prefix + " a été changé par " + p;
    	
    	prefix = p;
    	
    	e.getJDA().getPresence().setGame(Game.watching(prefix + "help"));
    	build.setColor(0x00d2ff);
		build.setDescription(message);
		msgChannel.sendMessage(build.build()).queue();
    }
    
    
    
    public void poll (MessageReceivedEvent e, Message msg, MessageChannel msgChannel, User msgUser, List<Member> mentionedMembers, String[] orders) {
    	
    	EmbedBuilder build = new EmbedBuilder();
		build.setColor(0xfffd00);
		build.setFooter("Poll de " + msgUser.getName(), msgUser.getAvatarUrl());
		
		String mes = "";
		if (orders.length > 2) {
			for (int i = 2; i < orders.length; i++) {
				mes = mes + letters[i-2] + " " + orders[i] + System.getProperty("line.separator");
			}
			build.addField("**" + orders[1] + "**", mes, false);
			Message m = new MessageBuilder().append("Un nouveau poll a été lancé!").setEmbed(build.build()).build();
			msgChannel.sendMessage(m).queue();
			List<Message> sendMessages = msgChannel.getHistory().retrievePast(3).complete();
			Message sendMes = sendMessages.get(0);
			for (int i = 2; i < orders.length; i++) {
				sendMes.addReaction(lettersUnicode[i-2]).queue();
			}
		} else {
			build.setTitle("**" + orders[1] + "**");
			Message m = new MessageBuilder().append("Un nouveau poll a été lancé!").setEmbed(build.build()).build();
			msgChannel.sendMessage(m).queue();
			List<Message> sendMessages = msgChannel.getHistory().retrievePast(3).complete();
			Message sendMes = sendMessages.get(0);
			sendMes.addReaction("\uD83D\uDC4D").queue();
			sendMes.addReaction("\uD83D\uDC4E").queue();
		}
			
    }
    
    
    
    public void tirage(MessageReceivedEvent e, Message msg, MessageChannel msgChannel, User msgUser, String[] orders) {
    	Message m;
    	EmbedBuilder build = new EmbedBuilder();
	build.setColor(0x4beea6);
	build.setFooter("Tirage pour " + msgUser.getName(), msgUser.getAvatarUrl());
	String choix = "";
	
	choix = choix + orders[1];
	for (int i = 2; i < orders.length;  i++) {
		choix = choix + ", " + orders[i];
	}
	 
	String res;
	if (orders.length == 2) {
		res = orders[1];
	} else {
		res = orders[rand.nextInt(orders.length - 1) + 1];
	}
	
	build.setTitle("**" + res + "**");
	
	m = new MessageBuilder().append("Le résultat du tirage entre " + "**" + choix + "**" + " est ...").setEmbed(build.build()).build();
	msgChannel.sendMessage(m).queue();
    }
    
    
    
    public void rule34(MessageReceivedEvent e, Message msg, MessageChannel msgChannel, User msgUser, String[] orders) throws IOException, SAXException, ParserConfigurationException {
    	Message m;
	int safe = 0;
	int timeout_limit = 100;
    	if (e.getTextChannel().isNSFW()) {
        	EmbedBuilder build = new EmbedBuilder();
		build.setTitle("Trouvé sur rule34.xxx", "https://rule34.xxx/");
    		build.setColor(0xfc0cee);
    		build.setFooter("Demandé par " + msgUser.getName(), msgUser.getAvatarUrl());
    		String tag = "";
    		String imageUrl = "";
    		String uri = "https://rule34.xxx/?page=dapi&s=post&q=index&limit=100";
    		
    		if (orders.length >= 2) {
    			tag = tag + orders[1];
    			uri = uri + "&tags=" + orders[1].replaceAll("/", "%2f").replace(" ", "_");;
    		}
    		for (int i = 2; i < orders.length; i++) {
    			tag = tag + ", " + orders[i];
    			uri = uri + "+" + orders[i].replaceAll("/", "%2f").replace(" ", "_");;
    		}
    		
    		URL url = new URL(uri);
    		HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();

    		httpConnection.setRequestMethod("GET");
    		httpConnection.setRequestProperty("Accept", "application/xml");

    		InputStream xml = httpConnection.getInputStream();
    		
    		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    		DocumentBuilder db = dbf.newDocumentBuilder();
    		Document doc = db.parse(xml);
    		doc.getDocumentElement().normalize();
    		
		int pid = 0;
		int count = Integer.parseInt(doc.getDocumentElement().getAttribute("count"));
    		if (count > 200000) {
    			pid = rand.nextInt(2000);
    		} else if (count > 100) {
    			pid = rand.nextInt(count/100);
    		} else {
			pid = 0;
		}
    		
		if (pid != 0){
			uri = uri + "&pid=" + pid;

			url = new URL(uri);
			httpConnection = (HttpURLConnection) url.openConnection();

			httpConnection.setRequestMethod("GET");
			httpConnection.setRequestProperty("Accept", "application/xml");

			xml = httpConnection.getInputStream();

			doc = db.parse(xml);
			doc.getDocumentElement().normalize();
		}
    		
		NodeList nodeList = doc.getDocumentElement().getChildNodes();
    		if (count > 0 && nodeList.getLength() > 0){
    			Node node;
    			int l;
			safe = 0;
    			NamedNodeMap nodeMap;
    			do {
        				do {
        					int x = rand.nextInt(nodeList.getLength());
						safe++;
            					node = nodeList.item(x);
        				} while (node instanceof com.sun.org.apache.xerces.internal.dom.DeferredTextImpl && safe < timeout_limit);
					if (safe >= timeout_limit) {
						Message timeout;
    						timeout = new MessageBuilder().append("Timeout").build();
    						msgChannel.sendMessage(timeout).queue();
						return;
					}
        				nodeMap = node.getAttributes();
        				nodeMap.getNamedItem("file_url");
        				imageUrl = nodeMap.getNamedItem("file_url").toString().substring(10);
            				l = imageUrl.length();
            				imageUrl = imageUrl.substring(0, l-1);
        		} while (!imageUrl.substring(l-5).equals("jpeg") && !imageUrl.substring(l-4).equals("png") && !imageUrl.substring(l-4).equals("jpg")  && (safe < timeout_limit));
    		
    			build.setImage(imageUrl);
    
    			if (!tag.equals("")) {
    				m = new MessageBuilder().append("Voici les résultats de ma recherche avec les tags **" + tag + "**").setEmbed(build.build()).build();
    			} else {
    				m = new MessageBuilder().append("Voici les résultats de ma recherche sans tags").setEmbed(build.build()).build();
    			}
    			msgChannel.sendMessage(m).queue();
    		} else {
    			Message error;
    			if (!tag.equals("")) {
    				error = new MessageBuilder().append("Aucun résultat avec les tags **" + tag + "**").build();
    			} else {
    				error = new MessageBuilder().append("Aucun résultat sans tags (wtf?)").build();
    			}
    			msgChannel.sendMessage(error).queue();
    		}
    	} 
    }
    
    
    
    public void wp(MessageReceivedEvent e, Message msg, MessageChannel msgChannel, User msgUser, String[] orders) throws IOException, SAXException, ParserConfigurationException {
		Message m;
        	EmbedBuilder build = new EmbedBuilder();
		build.setTitle("Trouvé sur Wallpaper Abyss", "https://wall.alphacoders.com/");
    		build.setColor(0x956294);
    		build.setFooter("Demandé par " + msgUser.getName(), msgUser.getAvatarUrl());
    		String tag = "";
    		String imageUrl = "";
    		String uri = "https://wall.alphacoders.com/api2.0/get.php?auth=" + System.getenv("IMAGE_TOKEN");
		int page = 1;
		
		if (orders.length == 1) {
			uri = uri + "&method=random&count=1";
		} 
		
    		if (orders.length >= 2) {
    			tag = tag + orders[1];
    			uri = uri + "&method=search&term=" + orders[1].replace(" ", "+");
			for (int i = 2; i < orders.length; i++) {
				tag = tag + ", " + orders[i];
    				uri = uri + "+" + orders[i].replace(" ", "+");
			}
    		}
    		
    		URL url = new URL(uri);
    		HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();

    		httpConnection.setRequestMethod("GET");
    		httpConnection.setRequestProperty("Accept", "application/json");

    		InputStream response = httpConnection.getInputStream();
		Reader reader = new InputStreamReader(response);
		String json = CharStreams.toString(reader);
		JSONObject jsonObject = new JSONObject(json);
    		
		int count = 1;
		if (orders.length > 1) {
			count = Integer.parseInt(jsonObject.getString("total_match"));
			if (count > 3000) {
				page = rand.nextInt(100) + 1;
			} else if (count > 30) {
				page = rand.nextInt(count/30) + 1;
			} else {
				page = 1;
			}
		}
		
		
		if ((orders.length > 1) && (page > 1)){
			uri = uri + "&page=" + page;
	
			url = new URL(uri);
			httpConnection = (HttpURLConnection) url.openConnection();
	
			httpConnection.setRequestMethod("GET");
			httpConnection.setRequestProperty("Accept", "application/json");
	
			response = httpConnection.getInputStream();
			reader = new InputStreamReader(response);
			json = CharStreams.toString(reader);
			jsonObject = new JSONObject(json);
		}
    		
		
    		if (count > 0){
			int x;
			if (orders.length == 1) {
				x = 0;
			} else {
    				x = rand.nextInt(jsonObject.getJSONArray("wallpapers").length());
			}
			imageUrl = jsonObject.getJSONArray("wallpapers").getJSONObject(x).getString("url_image");
			
    			build.setImage(imageUrl);
    		
    			if (!tag.equals("")) {
    				m = new MessageBuilder().append("Voici les résultats de ma recherche avec le tags **" + tag + "**").setEmbed(build.build()).build();
    			} else {
    				m = new MessageBuilder().append("Voici les résultats de ma recherche sans tags").setEmbed(build.build()).build();
    			}
    			msgChannel.sendMessage(m).queue();
    		} else {
    			Message error;
    			if (!tag.equals("")) {
    				error = new MessageBuilder().append("Aucun résultat avec les tags **" + tag + "**").build();
    			} else {
    				error = new MessageBuilder().append("Aucun résultat sans tags (wtf?)").build();
    			}
    			msgChannel.sendMessage(error).queue();
    		}
    	}
}
