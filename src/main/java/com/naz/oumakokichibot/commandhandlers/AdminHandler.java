package com.naz.oumakokichibot.commandhandlers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.math.NumberUtils;

import com.naz.oumakokichibot.Const;
import com.naz.oumakokichibot.NUtils;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class AdminHandler {

	public static void handle(MessageReceivedEvent event, String message) {
		switch (message.split(" ")[0].toLowerCase()) {
			case "execute": 
			case "execution": {
				if (!event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) {
					event.getTextChannel().sendMessage("I need the manager message ...").queue();
					return;
				}
				
				message = NUtils.removeStart(message);
								
				if (message == null) {
					EmbedBuilder executeError = new EmbedBuilder();
					executeError.addField(Const.PREFIX + "execution <Number of Messages> [@Mention]",
						"Delete the number of messages as provided, if a user is mentioned; then remove x of only their messages!", false);
					event.getChannel().sendMessage(NUtils.buildEmbed(executeError)).queue();
					return;
				}
				// Grab the first word of whatever is left in message, also replace all commas with nothing, eg 1,000,000 becoms 1000000.
				// This is because to make an int, or double, or whatever numerical data type, it can't take commas still.
				String killMessage = message.split(" ")[0].replace(",", "");
				
				
				// Is killMessage a valid number!
				if (!NumberUtils.isCreatable(killMessage)) {
					event.getChannel().sendMessage("You sent me an invalid number, dumbass.").queue();
					return;
				}
				// Convert killMessage to a number as it was previously treated as a line of text.
				int succNumber = Integer.parseInt(killMessage);
				
				if (succNumber < 2 || succNumber > 100) {
					event.getChannel().sendMessage("You asked for a number outside of my paramaters. Try something between 2~100.").queue();
					return;
				}
				// To-Do: Don't try to delete messages older than 2 weeks. (We'll get errors if we do try to do this.)
				if (event.getMessage().getMentionedUsers().isEmpty()) {
					event.getTextChannel().getHistory().retrievePast(succNumber).queue(del ->
						event.getTextChannel().deleteMessages(del).queue()
					);
				} else {
					User user = event.getMessage().getMentionedUsers().get(0);
					List<Message> toDelete = new ArrayList<>();
					event.getTextChannel().getHistory().retrievePast(100).queue(del -> {
						toDelete.addAll(del.stream().filter(o -> o.getAuthor() == user).collect(Collectors.toList()));
						if (toDelete.size() > succNumber)
							toDelete.subList(succNumber, toDelete.size()).clear();
						event.getTextChannel().deleteMessages(toDelete).queue();
					});
				} 
				
				break;
			} 
			default: {

				/*
				 * Usually when making a swich/case (this) you have to make sure
				 * to have a default case which handled anything which isn't otherwise
				 * handled by the cases scpecified. In this case it's empty
				 * cause we don't really give a shit about the message if they didn't
				 * do a command. They only get attention if they guve Ouma attention. >:C
				 */
				
				break;
			}
		}
	}
}
