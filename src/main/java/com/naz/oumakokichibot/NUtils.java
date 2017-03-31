 package com.naz.oumakokichibot;

import java.awt.Color;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

public class NUtils {
	
	/**
	 * This is how you create an enum, an enum basically like creating
	 * your own data type with a specific set of possible values.
	 * For example, if we create a DayOfTheWeek object, other than null
	 * which means no pointer. It can never be a value that is not
	 * defined in the list.
	 */
	
	public enum DayOfTheWeek {
		MONDAY, TUEDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
	}
	
	/**
	 * This is how we create a DayOfTheWeek, do note we still have to access
	 * the MONDAY value through the DayOfTheWeek enum, we can't just type
	 * Monday stright up - if no value is set, it is null.
	 */
	
	public DayOfTheWeek day = DayOfTheWeek.MONDAY;
	
	/**
	 * This allows us to generate random numbers.
	 */
	
	public static Random rng = new Random();
	
	/**
	 * Message Builder reused when building embed.
	 */
	
	private static MessageBuilder builder = new MessageBuilder();
	
	/**
	 * Color used for each Embed.
	 */
	
	private static Color embedColor = new Color(102, 51, 153);
	
	/**
	 * Checks if the first String passed to the fuction
	 * begins with the second String while ignoring
	 * any capitilization.
	 * 
	 * @param message	The string to check.
	 * @param prefix	The prefix to check for.
	 * @return			If the string began with the prefix.
	 */
	
	public static boolean startNoCase(String message, String prefix) {
		if (message == null || prefix == null)
			return false;
		
		return message.toLowerCase().startsWith(prefix.toLowerCase());
	}
	
	public static boolean containsNoCase(String message, String contains) {
		if (message == null || contains == null)
			return false;
		
		return message.toLowerCase().contains(contains.toLowerCase());
	}

	/**
	 * Takes a String and returns the same String with the first
	 * word removed - if after removing the first word the result
	 * is an empty String, returns null instead.
	 * 
	 * @param 	message		String to modify.
	 * @return				String with first word removed, or null
	 * 						if the String is empty.
	 */
	
	public static String removeStart(String message) {
		message = message.substring(message.split(" ")[0].length()).trim();
		return StringUtils.isWhitespace(message) ? null : message;
	}
	
	/**
	 * The way JDA handles embed building sucks butt so we're going
	 * to build embeds using this function instead.
	 * 
	 * @param 	embed	The filled EmbedBuilder object.
	 * @return			The message to send.
	 */
	
	public static Message buildEmbed(EmbedBuilder embed) {
		return builder.setEmbed(embed.setColor(embedColor).build()).build();
	}
	
	/**
	 * public = accesible projectwide <br>
	 * static = allows you to access the function without creating
	 * 			an instance of that object as technically only
	 * 			one of this function exists and that function will
	 * 			never change - every instance of this class, including
	 * 			the static instance shares this function and variables
	 * 			related to it. <br>
	 * boolean = the beggining of an equation that returns true || false <br>
	 * isElevated = returns bolean. if mem is adm/mang/own will be true;
	 * 				if not, they a fake ass false bitch
	 *
	 * @param 	mngrUp	
	 * @return
	 */
	
	public static boolean isElevated(Member mngrUp) {
		// return if member has admin permission or if member has manage server permission or if member is owner
		return mngrUp.hasPermission(Permission.ADMINISTRATOR) || mngrUp.hasPermission(Permission.MANAGE_SERVER) || mngrUp.isOwner(); // < sad face
	}
	
	/**
	 * Searches the members of the guild provided
	 * and sees if the search term provided is in the name
	 * of any of the users. - This starts by checking ID
	 * then discrimnator, and finall for every member
	 * check the nickname and username. If after all
	 * of these checks no user is found - return null.
	 * 
	 * @param guild		The guild to check the mmebers of.
	 * @param search	The search term.
	 * @return			The member.
	 */
	
	public static Member searchUsers(Guild guild, String search) {
		if (search == null || guild == null)
			return null;
		
		Member user;
		
		if (NumberUtils.isCreatable(search)) {
			user = guild.getMemberById(search);
			
			if (user != null)
				return user;
			
			if (search.length() == 4) {
				// For every member inside the guild
				for (Member member : guild.getMembers()) {
					if (member.getUser().getDiscriminator().equals(search))
						return member;
				
				}
			}
		}
		
		for(Member member : guild.getMembers()) {
			if (containsNoCase(member.getEffectiveName(), search))
				return member;
			
			if (containsNoCase(member.getUser().getName(), search))
				return member;
		}
		
		return null;
	}
}
