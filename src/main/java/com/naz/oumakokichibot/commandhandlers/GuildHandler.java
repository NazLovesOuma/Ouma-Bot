package com.naz.oumakokichibot.commandhandlers;

import java.util.stream.Collectors;

import com.naz.oumakokichibot.NUtils;

import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class GuildHandler {
	public static void handle(MessageReceivedEvent event, String message) {
		switch (message.split(" ")[0].toLowerCase()) {
			case "emotes":
			case "emojies": {				
				if (event.getGuild().getEmotes().isEmpty())
					event.getTextChannel().sendMessage("There are no emotes!").queue();
				else
					// Send to the channel the message was sent from - for every emote in the guild, stream through the list adn instead of an array
					// of Emotes - turn it into a list of Strings which have the values of that emote, as a mention. (eg as, ":8ball:", and join all the
					// emotes together as thought it was a single string.
					event.getTextChannel().sendMessage(event.getGuild().getEmotes().stream().map(Emote::getAsMention).collect(Collectors.joining())).queue();
				break;
			}
			case "add":
			case "remove":
			case "queue":
            case "skip": {
            	// Go to the musicHandler function below and bring the two parameters with it
            	musicHandler(event, message);
            	break;
            } 
            case "mute": {
            	message = NUtils.removeStart(message);
            	muteHandler(event, message);
            	break;
            }
			default: {
				// Check if teh user that sent the message was an admin, manager or owner of the server
				// If so check if they did an admin only comamad.
				if (NUtils.isElevated(event.getMember()))
					AdminHandler.handle(event, message);
			}
		}
	}
	
	public static void musicHandler(MessageReceivedEvent event, String message) {
		switch (message.split(" ")[0].toLowerCase()) {
			case "add": {
				
				break;
			}
			case "remove": {
				
				break;
			}
			case "queue": {
				
				break;
			}
			case "skip": {
				
				break;
			}
			default: {
				// Should be impossible to get here.
				break;
			}
		}
	}
	
	public static void muteHandler(MessageReceivedEvent event, String message) {
		switch (message.split(" ")[0].toLowerCase()) {
			case "add": {
				
				break;
			}
			case "remove": {
				
				break;
			}
			case "list": {
				
				break;
			}
			default: {
				// Should be impossible to get here.
				break;
			}
		}
	}
}
