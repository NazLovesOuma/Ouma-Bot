package com.naz.oumakokichibot;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.naz.oumakokichibot.commandhandlers.AllHandler;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class OumaBot extends ListenerAdapter {
	
	/*
	 * public - 	Accessed across the entire program.
	 * 				No matter where that file may be, nor
	 * 				where that object was instantiated or
	 * 				if that .java/class was kept in a different
	 * 				package/folder.
	 * 
	 * private -	Only this class will have access to the field.
	 * 				Even if you make an object of a class with a private
	 * 				field in it, you will never be able to access
	 * 				the fields that are private. 			
	 */
	
	public static JDA jda;
	
	/**
	 * The total number of messages Ouma has read, be it
	 * Private Messages or Guild Messages.
	 */
	
	public static int totalMessagesRead = 0;
	/**
	 * Ensure not to add tild in CustomCommand map!
	 * See: {@link #main(String[])} - do "customCommands.put(x, y);"
	 * x being the command, and y being the response.
	 */
	
	private static Map<String, String> customCommands = new HashMap<>();
	
	/**
	 * These are the different responses that may occur when
	 * someone mentions Ouma bot.
	 */
	
	public static String[] mentionReponses = {
		"Type " + Const.PREFIX + "help to see my full list of commands."
	};
	
	public static void main(String[] agrs) {
		try {
			// Login to Discord using your bot and set the game!
			jda = new JDABuilder(AccountType.BOT).setToken(Const.BOT_TOKEN)
				.setGame(Game.of( Const.PREFIX + "help or ping me.")).addListener(new OumaBot()).buildAsync();
			// Put your custom commands here!
		} catch (Exception ex) {
			ex.printStackTrace();
			// Should the bot fail to log in, turn off the program.
			System.exit(1);
		}
	}
	
	/**
	 * This executes whenever the bot has finished loading,
	 * which should only be once. It prints to the console
	 * that the bot has succesfully logged in.
	 */
	
	@Override
	public void onReady(ReadyEvent event) {
		System.out.println("The bot has succesfully logged in!");
	}
	
	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		
		/*
		 * 1 Ouma: ty for inviting me to your guild ping me for assistance or use my help feature
		 * change my prefix by doing 'changeprefix <new prefix>'
		 * see my settings for more or check out my github link (link)
		 */
		
		EmbedBuilder embed = new EmbedBuilder();
		
		/*
		 * [NAME HERE](URL HERE) - To make a piece of text hyperlink to a URL.
		 */
		
		embed.setDescription("Thank you for the invite~. Ping me for assistance or use " + Const.PREFIX +  "help!"
			+ " Check out my [GitHub](https://github.com/NazLovesOuma/Ouma-Bot/wiki/Help) to see my full list of commands!");
		
		event.getGuild().getPublicChannel().sendMessage(NUtils.buildEmbed(embed)).queue();
		
	}
	
	/**
	 * Whenever a member has joined the guild, welcome
	 * that member.
	 */
	
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		Guild guild = event.getGuild();
		
		// Check if the user that joined was a bot, if so mention it's a bot in the message.
		if (event.getMember().getUser().isBot())
			guild.getPublicChannel().sendMessage("Welcoming the new bot, " + event.getMember().getAsMention() + " to " + guild.getName() + "~.").queue();
		else
			guild.getPublicChannel().sendMessage("Welcome, " + event.getMember().getAsMention() + " to " + guild.getName() + "~.").queue();
	}
	
	@Override
	public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
		// If id of the member that left is equal to the id of your bot it self
		if (event.getMember().getUser().getId() == jda.getSelfUser().getId())
			// Exit the function
			return;
		// send to the guilds public channel (usually general) "bye bye" with a mention to the user that left
		event.getGuild().getPublicChannel().sendMessage("Bye bye, " + event.getMember().getAsMention() + "~.").queue();
	}
	
	/**
	 * Goes off for everything single message the bot has read.
	 * This applies to both guilds and private messages.
	 */
		
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		// ++ Means to add 1 to. - Add 1 to the totalMessagesRead count.
		totalMessagesRead++;
		
		// Cache the author of the message to a seperate varaible to easier access.
		User user = event.getAuthor();
		MessageChannel channel = event.getChannel();
		String message = event.getMessage().getContent();
		
		// If the user is a bot, ignore and return!
		if (user.isBot())
		    return;
		
		/* 
		 * If the message mentions Ouma, and doesn't mention everyone, send a random message from
		 * mentionResponses and then return, ignoring the rest of this function. If it doesn't, check
		 * if the message starts with hi or hello, if so respond and return.
		 */
		
		if (event.getMessage().isMentioned(jda.getSelfUser()) && !event.getMessage().mentionsEveryone() && !message.startsWith(Const.PREFIX)) {
			channel.sendMessage(mentionReponses[NUtils.rng.nextInt(mentionReponses.length)]).queue();
			return;
		} else if(message.matches("(?i)hi|hello(?![\\w\\d])")) {
			channel.sendMessage("Hello~!").queue();
			return;
		}

		/*
		 * Any code which is put before this comment doesn't require the prefix.
		 * Anything after however does.
		 */
		
		// If message does not start withour prefix
		if (!message.startsWith(Const.PREFIX))
			return;

		/*
		 * At this point, the message no longer starts with the prefix as
		 * we remove it entirely. Eg: "~help" becomes "help"
		 */
		
		// Remove the length of your prefix from the beggining of our message.
		message = message.substring(Const.PREFIX.length());
		
		/*
		 * Iterate through every entry (command) in the customCommand
		 * hashmap created and filled upon launching this application
		 * and if the message matches any of the keys (while ignore case)
		 * send that keys value and return.
		 */
		
		// For everty entry in the custom command map, check if the message
		// is equal to the key, if so, send the value.
		for (Entry<String, String> entry : customCommands.entrySet()) {
			if (message.equalsIgnoreCase(entry.getKey())) {
				channel.sendMessage(entry.getValue().replace("<MENTION>", user.getAsMention())).queue();
				return;
			}
		}
		
		/*
		 * AllHandler will be the static handler we use for
		 * commands that can be possible in Guilds and PMs.
		 * There will be other handlers that may cover Guild only
		 * commands or only for Guild Admins, or for the bot dev.
		 */
		
		AllHandler.handle(event, message);
	}
}

