package Justice.JusticeBot;

import java.io.IOException;
import java.io.InputStream;
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
		MessageChannel msgChannel = e.getChannel();
		User msgUser = e.getAuthor();
		List<Member> mentionedMembers = msg.getMentionedMembers();
		
       		String msgString = msg.getContentRaw();
    		if (msgString.length() >= prefix.length() && msgString.substring(0, prefix.length()).equals(prefix)) {	//on a une commande
			String[] tmp = msgString.split(Character.toString('"'), 0);
			int size = 0;

			for (int i = 0; i < tmp.length; i++) {
				if (!tmp[i].equals(" ")) {
					size++;
				}
			}

			int r = 0;
			String[] orders = new String[size];
			for (int i = 0; i < tmp.length; i++) {
				if (!tmp[i].equals(" ")) {
					orders[r] = tmp[i];
					r++;
				}
			}
    			if (orders[0].equals("prefixe")) {
				msg.delete().queue();
    				if (orders.length >= 2)
    					prefixe(orders[1], e, msgChannel);
    			} /*else if (orders[0].equals("punir") && e.getMember().hasPermission(Permission.ADMINISTRATOR)) {
    				punir(e, msg, msgChannel, msgUser, mentionedMembers);
    			} else if (orders[0].equals("pardon") && e.getMember().hasPermission(Permission.ADMINISTRATOR)) {
    				pardon(e, msg, msgChannel, msgUser, mentionedMembers);
    			}*/ else if (orders[0].equals(prefix + "poll ") && orders.length >= 2 && orders.length <= 27) {
				msg.delete().queue();
    				poll(e, msg, msgChannel, msgUser, mentionedMembers, orders);
    			} else if (orders[0].equals(prefix + "tirage ") && orders.length >= 2) {
				msg.delete().queue();
    				tirage(e, msg, msgChannel, msgUser, orders);
    			} else if ((orders[0].equals(prefix + "rule34 ")) || (orders[0].equals(prefix + "rule34"))) {
				msg.delete().queue();
    				rule34(e, msg, msgChannel, msgUser, orders);
    			} else if (((orders[0].equals("/zerochan ") || (orders[0].equals("/zerochan")) && orders.length <= 2)) {
				msg.delete().queue();
    				zerochan(e, msg, msgChannel, msgUser, orders);
    			} else if (orders[0].equals("/help")) {
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
		/*build.addField(prefix + "punir", "Enlève la citoyenneté et met en Sous-Race (Administrateur requis)", false);
		build.addField(prefix + "pardon", "Enlève Sous-Race et met la citoyenneté (Administrateur requis)", false);*/
		build.addField(prefix + "poll \"Question\" \"Réponse 1\" \"Réponse 2\" ...", "Effectue un poll", false);
		build.addField(prefix + "tirage \"Proposition 1\" \"Proposition 2\" ...", "Effectue un tirage au sort parmi les propositions données", false);
		build.addField(prefix + "rule34 \"tags\"", "Recherche une image sur rule34 en utilisant les tags fournis", false);
		build.addField(prefix + "zerochan \"tag\"", "Recherche une image sur konachan en utilisant le tag fournis (attention, un seul tag supporté", false);
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
    
    /*public void punir(MessageReceivedEvent e, Message msg, MessageChannel msgChannel, User msgUser, List<Member> mentionedMembers) {
    	EmbedBuilder build = new EmbedBuilder();
		
    	String names = "";
		Iterator<Member> iter = mentionedMembers.iterator();
		while (iter.hasNext()) {
			Member currentMember = iter.next();
			if (names.contentEquals("")) {
				names = names + currentMember.getAsMention();
			} else {
				names = names + ", " + currentMember.getAsMention();
			}
			e.getGuild().getController().modifyMemberRoles(currentMember, e.getGuild().getRolesByName("non", true), e.getGuild().getRolesByName("oui", true)).queue();
		}
	
		build.setColor(0xff4600);
		if (mentionedMembers.size() == 0) {
			build.setDescription("Personne n'a été ban");
		} else {
			build.setDescription(names + " a(ont) été ban.");
		}
		msgChannel.sendMessage(build.build()).queue();
    }
    
    public void pardon(MessageReceivedEvent e, Message msg, MessageChannel msgChannel, User msgUser, List<Member> mentionedMembers) {
    	EmbedBuilder build = new EmbedBuilder();
    	
    	String names = "";
		Iterator<Member> iter = mentionedMembers.iterator();
		while (iter.hasNext()) {
			Member currentMember = iter.next();
			if (names.contentEquals("")) {
				names = names + currentMember.getAsMention();
			} else {
				names = names + ", " + currentMember.getAsMention();
			}
			e.getGuild().getController().modifyMemberRoles(currentMember, e.getGuild().getRolesByName("Citoyen", false), e.getGuild().getRolesByName("Sous-Race", false)).queue();
		}
	
		build.setColor(0x53ff00);
		if (mentionedMembers.size() == 0) {
			build.setDescription("Personne n'a été deban");
		} else {
			build.setDescription(names + " a(ont) été deban.");
		}
		msgChannel.sendMessage(build.build()).queue();
    }*/
    
    public void poll (MessageReceivedEvent e, Message msg, MessageChannel msgChannel, User msgUser, List<Member> mentionedMembers, String[] orders) {
    	
    	EmbedBuilder build = new EmbedBuilder();
		build.setColor(0xfffd00);
		build.setFooter("Poll de " + msgUser.getName(), msgUser.getAvatarUrl());
		
		String mes = "";
		if (orders.length > 2) {
			for (int i = 2; i < orders.length; i++) {
				mes = mes + letters[i-2] + orders[i] + System.getProperty("line.separator");
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
    			NamedNodeMap nodeMap;
    			do {
        				do {
        					int x = rand.nextInt(nodeList.getLength());
            				node = nodeList.item(x);
        				} while (node instanceof com.sun.org.apache.xerces.internal.dom.DeferredTextImpl);
        				nodeMap = node.getAttributes();
        				nodeMap.getNamedItem("file_url");
        				imageUrl = nodeMap.getNamedItem("file_url").toString().substring(10);
            			l = imageUrl.length();
            			imageUrl = imageUrl.substring(0, l-1);
        			} while (!imageUrl.substring(l-5).equals("jpeg") && !imageUrl.substring(l-4).equals("png") && !imageUrl.substring(l-4).equals("jpg"));
    		
    			build.setImage(imageUrl);
    		
    			if (!tag.equals("")) {
    				m = new MessageBuilder().append("Voici les résultats de ma recherche avec les tags : " + tag).setEmbed(build.build()).build();
    			} else {
    				m = new MessageBuilder().append("Voici les résultats de ma recherche sans tags").setEmbed(build.build()).build();
    			}
    			msgChannel.sendMessage(m).queue();
    		} else {
    			Message error;
    			if (!tag.equals("")) {
    				error = new MessageBuilder().append("Aucun résultat avec les tags : " + tag).build();
    			} else {
    				error = new MessageBuilder().append("Aucun résultat sans tags (wtf?)").build();
    			}
    			msgChannel.sendMessage(error).queue();
    		}
    	} else {
    		m = new MessageBuilder().append(msgUser.getAsMention() + ", ce n'est pas un canal NSFW").build();
		msgChannel.sendMessage(m).queue();
    	}
    }
    
    public void zerochan(MessageReceivedEvent e, Message msg, MessageChannel msgChannel, User msgUser, String[] orders) throws IOException, SAXException, ParserConfigurationException {
		Message m;
        	EmbedBuilder build = new EmbedBuilder();
		build.setTitle("Trouvé sur Zerochan.net", "http://zerochan.net/");
    		build.setColor(0x956294);
    		build.setFooter("Demandé par " + msgUser.getName(), msgUser.getAvatarUrl());
    		String tag = "";
    		String imageUrl = "";
    		String uri = "http://zerochan.net/";
		int page = 1;
		
    		if (orders.length == 2) {
    			tag = tag + orders[1];
    			uri = uri + orders[1].replace(" ", "+");;
    		}
		
		uri = uri + "?xml";
		
		if (orders.length == 1) {
			page = rand.nextInt(1000) + 1;
			uri = uri + "&p=" + page;
		} 
    		
    		URL url = new URL(uri);
    		HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();

    		httpConnection.setRequestMethod("GET");
    		httpConnection.setRequestProperty("Accept", "application/rss+xml");

    		InputStream xml = httpConnection.getInputStream();
    		
    		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    		DocumentBuilder db = dbf.newDocumentBuilder();
    		Document doc = db.parse(xml);
    		doc.getDocumentElement().normalize();
    		
		int count = 1;
		if (orders.length > 1) {
			String desc = doc.getDocumentElement().getChildNodes().item(1).getChildNodes().item(7).getTextContent();
			String[] descTab = desc.substring(13).split(" ");
			String[] nbImageTab = descTab[0].split(",");
			String nbImageStr = "";
			for (int i = 0; i < nbImageTab.length; i++) {
				nbImageStr = nbImageStr + nbImageTab[i];
			}
			
			count = Integer.parseInt(nbImageStr) ;
			if (count > 100000) {
				page = rand.nextInt(1000) + 1;
			} else if (count > 100) {
				page = rand.nextInt(count/100) + 1;
			} else {
				page = 1;
			}
		}
		
		
		if ((orders.length > 1) && (page > 1)){
			uri = uri + "&p=" + page;
	
			url = new URL(uri);
			httpConnection = (HttpURLConnection) url.openConnection();
	
			httpConnection.setRequestMethod("GET");
			httpConnection.setRequestProperty("Accept", "application/rss+xml");
	
			xml = httpConnection.getInputStream();
	
			doc = db.parse(xml);
			doc.getDocumentElement().normalize();
		}
    		
		NodeList nodeList = doc.getDocumentElement().getChildNodes();
    		if (count > 0 && nodeList.getLength() > 0){
    			Node node, content;
			int l;
			do {
    				do {
    					int x = 2*rand.nextInt(nodeList.getLength()/2) + 1;
        				node = nodeList.item(x);
    				} while (!node.getNodeName().equals("item"));
    				content = node.getChildNodes().item(13);
    				imageUrl = content.getAttributes().getNamedItem("url").toString().substring(5);
        			l = imageUrl.length();
        			imageUrl = imageUrl.substring(0, l-1);
    			} while (!imageUrl.substring(l-5).equals("jpeg") && !imageUrl.substring(l-4).equals("png") && !imageUrl.substring(l-4).equals("jpg"));
		
			Message tmpMes = new MessageBuilder().append(imageUrl).build();
			msgChannel.sendMessage(tmpMes).queue();
			
    			build.setImage(imageUrl);
    		
    			if (!tag.equals("")) {
    				m = new MessageBuilder().append("Voici les résultats de ma recherche avec le tag : " + tag).setEmbed(build.build()).build();
    			} else {
    				m = new MessageBuilder().append("Voici les résultats de ma recherche sans tag").setEmbed(build.build()).build();
    			}
    			msgChannel.sendMessage(m).queue();
    		} else {
    			Message error;
    			if (!tag.equals("")) {
    				error = new MessageBuilder().append("Aucun résultat avec le tag : " + tag).build();
    			} else {
    				error = new MessageBuilder().append("Aucun résultat sans tag (wtf?)").build();
    			}
    			msgChannel.sendMessage(error).queue();
    		}
    }
}
