package com.naz.oumakokichibot.commandhandlers;


import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.naz.oumakokichibot.Const;
import com.naz.oumakokichibot.NUtils;
import com.naz.oumakokichibot.OumaBot;
import com.sethsutopia.utopiai.HttpResponse;
import com.sethsutopia.utopiai.UrbanDictionary;
import com.sethsutopia.utopiai.bing.Bing;
import com.sethsutopia.utopiai.google.youtube.Media;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class AllHandler {
	
	/**
	 * Create the UrbanDictionary object for defining
	 * words using the Urban Dictionary API. <br>
	 * Note from Sethy. <3
	 * Remember {@link UrbanDictionary#defineWord(String, boolean)}
	 * Pass true for a random word, or false for top voted definition.
	 */
	
	private static UrbanDictionary urban = new UrbanDictionary();
	
	/**
	 * Create the Bing object for access to the Bing Search API.
	 * This allows you to search for urls and images.
	 */
	
	private static Bing bing = new Bing(Const.BING_SEARCH_KEY);
		
	/**
	 * A list of responses, your code should dynamically
	 * adjust for if new entries appear or if old ones are removed.
	 * Remember: <> is for required arguments, and [] is for optional
	 * arguments. <br>
	 * MENTION - Replace with user mention.
	 */
	
	private static String[] fortune = {
		"'Yes.'",
		"'No.'",
		"'Maybe.'",
		"'Sometime in the future.'",
		"'Never.' :8ball:",
		"'It is destiny.'",
		"'Unknown at this time.'",
		"'Possibly.'",
		"'2% chance.'",
		"'Ask again.'",
		"'All signs point to yes, or maybe no.'",
		"'Without a doubt.'",
		"'Reply hazy. Try again later.'",
		"'10,000% Yes.'",
		"'Outlook is not good.'",
		"'I have no idea, yo.'"			
	};
	
	
	private static String[] compliments = {
		"Your looks crush me like pancakes, <MENTION>!",
		"I love you almost as much as I love Panta, <MENTION>!",
		"You're my favorite person ever, <MENTION>. Or maybe not. I'm a liar, ya' know?",
		"Let's run around naked across the school campus, <MENTION>.",
		"I'd treat you to a Panta anyday, <MENTION>.",
		"You're as cute as Kiibo, <MENTION>.",
		"I'm proud of you, <MENTION>. Keep it up~.",
		"I would *never* play a prank on you, <MENTION>~.",
		"You're the reason I get up everyday, <MENTION>.",
		"*patpat, <MENTION>"
	};
	
	public static void handle(MessageReceivedEvent event, String message) {
		switch (message.split(" ")[0].toLowerCase()) {
		
			/*
			 * By leaving an empty case above another case without a break
			 * allows for having multiple cases leading to the same code block.
			 */
		
			case "avatar":
			case "pfp": {
				// Remove the first word from our varaible message - and set message to that.
				message = NUtils.removeStart(message);
				
				// If: the list of mentioned users is empty.
				if (event.getMessage().getMentionedUsers().isEmpty())
					// give message the value of that authots avatar url.
					message =event.getAuthor().getAvatarUrl();
				else
					// Else: get every mentioned user, filter out users with no profile picture - then compile a list of users and set message to that.
					message = event.getMessage().getMentionedUsers().stream().filter(o -> o.getAvatarUrl() != null).map(User::getAvatarUrl).collect(Collectors.joining("\n"));
				
				// Is message equal to null? (The user(s) didn't have an avatar url)
				if (message == null)
					event.getChannel().sendMessage("Sethy just wanted to let you know that you have no profile picture and are "
						+ "therefore unable to use this command. as such, I must kindly ask that you bow down to him as he is right. If you "
							+ "want, though, you can ask him for some cool sites to get pfps from. Just hit him up. He's in my credits list.").queue();
				else
					event.getChannel().sendMessage(message).queue();
				
				break;
			}
			case "botstats":
			case "botinfo": {
				// Cache the user object for the developer of this bot.
				User user = event.getJDA().getUserById(Const.DEV_DISCORD_ID);
				
				// Create the embed builder, which is out tool to smoothly create an embed.
				EmbedBuilder embed = new EmbedBuilder();
				
				embed.setThumbnail("https://d111vui60acwyt.cloudfront.net/users/avatars/37095048/mktpl_large/tumblr_oih2uwucVk1viddefo3_250.png");
				
				embed.addField("Creator <3", user.getName() + "#" + user.getDiscriminator(), true);
				embed.addField("Ouma Bot", "Ouma Bot's character and personality is based on Ouma Kokichi from the game, Danganronpa V3.", true);
				embed.addField("Guilds", "Managing " + event.getJDA().getGuilds().size() + " Guilds" ,true);
				embed.addBlankField(false);
				embed.addField("Total HTTP Requests", String.valueOf(HttpResponse.getTotalRequests()), true);
				embed.addField("Total Messages Read", String.valueOf(OumaBot.totalMessagesRead), true);
				
				event.getChannel().sendMessage(NUtils.buildEmbed(embed)).queue();
				break;
			}
            case "coinflip": {
            	// Give the num the value of a random number from 0 to 99, then add 1.
            	int num = NUtils.rng.nextInt(100) + 1;
            		
        		if (num == 100)
        			event.getChannel().sendMessage("'The coin flew into oblivion!' - Ouma (Coin lost").queue();
        		else if (num == 99)
        			event.getChannel().sendMessage("'T-the coin fell on it's side!?' - Ouma (Tie)").queue();
        		else if (num > 3)
        			event.getChannel().sendMessage("'Nishishi~! I win again!' - Ouma (Bot wins)").queue();
        		else
        			event.getChannel().sendMessage("'Y-you w-w-won!? B-b-but this is rigged!' - Ouma (You won)").queue();
		
            	break;
            }
			case "dice": {
            	//Remove: message start
				message = NUtils.removeStart(message);
				//If: given no numbers, send the following error:
				if (message == null) {
					event.getChannel().sendMessage("2 numbers are required with a tild following the command. Ex: 1~10").queue();
					return;
				}
				//If: anything other than #~# is added, give the following error:
				message = message.replace(",", "");
				
				// Regex can be used to search for text by dictating rules, rather than literal text.
				if (!message.matches("[-]?[\\d]{1,}~[-]?[\\d]{1,}")) {
					event.getChannel().sendMessage("Paramaters were improperly formatted. Try: 1~10, for example.").queue();
					return;
				}
				//Split message for every '~' in #~#
				String[] split = message.split("~");
				
				/*
				 * parse == convert || handle
				 * Puts first half of split into 0, other in 1 
				 */
				
				int temp = Integer.parseInt(split[0]);
				int temp2 = Integer.parseInt(split[1]);
				
				if (temp > temp2) {
					event.getChannel().sendMessage("Made the first number smaller you fuck").queue();
					return;
				}
				
				/*
				 *convert text to numbers > creates datatype for this function, the NUtil rng
				 *.parseInt(#) changes Strings to ints (if valid)
				 *rng.nextInt(#) gives rng # from 0 to the number -1 Ex: rng.next(10) is from 0 to 9
				 *in the case of 20~50, the function does 50 - 20 = 30
				 *now it generates from 0 to 30 (which is really 0 to 29) +1 hence + temp + 1))
				 *then add minimum value which was 20
				 *giving us 50 
				 *_Seth is a cutie._
				 */
				
				event.getChannel().sendMessage(String.valueOf(NUtils.rng.nextInt(temp2 - temp) + temp + 1)).queue();			
				break;
	        }
			case "explain":
			case "define": {
				
				/*
				 * Once we've found out the message does start with define,
				 * or whatever other command, remove the command module itself.
				 * In this case, "explain panta" becomes "panta".
				 */
				
				message = NUtils.removeStart(message);
				
				/*
				 * NUtils.removeStart() will return null if the message has
				 * no text after removing the first word. For example if the user
				 * only typed: "define" - but nothing after. - If null is returned
				 * because of that, run this code to call them a fucking idiot and return.
				 */
				
				if (message == null) {
					event.getChannel().sendMessage("Explain what, now?").queue();
					return;
				}
				
				/*
				 * Cache a capitilised version of the word/phrase to define
				 * in order to have a more aesthtic response when displaying
				 * the definition. "Word: definition."
				 */
				
				String definition = StringUtils.capitalize(message);
				
				/*
				 * Use the urban dictionary defineWord function to actually return
				 * a definition of the word requested. The boolean (2nd argument passed)
				 * is if it should get a random word, or just the top voted.
				 * Despite the fact message was originally the users message,
				 * since we are finished with it, we are reusing that object
				 * in order to store the definition returned from the
				 * Urban Dictionary API. (No need to create a new Object when we can #Recyle)
				 */
				
				message = urban.defineWord(definition, true);
				
				/*
				 * It's possible urban dictionary didn't have a defintion for
				 * the word the user tried to look up, if so null will be returned.
				 * If null is returns apologise and return.
				 */
				
				if (message == null) {
					event.getChannel().sendMessage("No one has definied the term yet.").queue();
					return;
				}
				
				/*
				 * Doing definition += ": " + message;
				 * is the same as:
				 * definition = definition + ": " + message;
				 * 
				 * Another example of this is for example:
				 * int number = 10;
				 * 
				 * number = number + 5; (Which is 15)
				 * is the same as:
				 * number += 5; (Which is 15)
				 */
								
				// Definition gets the value of itself plus what's after the equal sign.
				definition += ": " + message;
				
				/*
				 * Since Discord gets mad at us when we try to send a message
				 * over 2,000 chars long, we check if the definition returns is over
				 * 2,000 chars, and if so, cut some of the message off so
				 * it's short enough for us to send.
				 */
				
				if (definition.length() > 2000)
					definition = definition.substring(0, 1997) + "...";
				
				event.getChannel().sendMessage(definition).queue();
				break;
			}
			case "image":
			case "picture": {
				
				/*
				 * Once we've found out the message does start with image or picture,
				 * or whatever other command, remove the command module itself.
				 * In this case, "image panta" becomes "panta".
				 */
				
				message = NUtils.removeStart(message);
				
				/*
				 * NUtils.removeStart() will return null if the message has
				 * no text after removing the first word. For example if the user
				 * only typed: "image" - but nothing after. - If null is returned
				 * because of that, run this code to call them a fucking idiot and return.
				 */
				
				if (message == null) {
					event.getChannel().sendMessage("I need a search term too, you butt!").queue();
					return;
				}
				
				
				/*
				 * Again, with the recyling variables thing as explained twice.
				 * The bing.imageSearch function will return an image however unlike
				 * bing.search() - the image search has an extra argument.
				 * The thing argument is if it should return a random image,
				 * or only the first result.
				 */
				
				message = bing.imageSearch(message, false, true);
				
				/*
				 * If there is no result, null was returned and so report there
				 * was no result and return!
				 */	
				 
				if (message == null) {
					EmbedBuilder embedOumaCry = new EmbedBuilder();
					embedOumaCry.setDescription("No results!");
					embedOumaCry.setImage("https://cdn.discordapp.com/attachments/283116800340787202/294294419308412928/tumblr_okbhq8uptN1vkx8d3o5_250.png"); 
					event.getChannel().sendMessage(NUtils.buildEmbed(embedOumaCry)).queue();
					return;
				}
				
				event.getChannel().sendMessage(message).queue();
				break;
			}
			case "search": {
				
				/*
				 * Once we've found out the message does start with search,
				 * or whatever other command, remove the command module itself.
				 * In this case, "search panta" becomes "panta".
				 */
				
				message = NUtils.removeStart(message);
				
				/*
				 * NUtils.removeStart() will return null if the message has
				 * no text after removing the first word. For example if the user
				 * only typed: "search" - but nothing after. - If null is returned
				 * because of that, run this code to call them a fucking idiot and return.
				 */
				
				if (message == null) {
					event.getChannel().sendMessage("I need a search term too, you butt!").queue();
					return;
				}
				
				/*
				 * Search Bing for the website requested. We also do the
				 * recyling variable thing here, since we're finished
				 * with message, we reuse that for storing the website url
				 * rather than wasting resources creating a new object reference.
				 * The boolean (2nd argument) in bing.search() is if the
				 * safe search should be set to strict.
				 */
				
				message = bing.search(message, false);
				
				/*
				 * Similarly with UrbanDictionary, there is always the chance
				 * that we might not have any results, in which case null will be returned.
				 * If so send that there were no results and return.
				 */
				
				if (message == null) {
					event.getChannel().sendMessage("No results!").queue();
					return;
				}
				
				event.getChannel().sendMessage(message).queue();
				break;
			}
			case "userinfo": {
            	message = NUtils.removeStart(message);
            	// Create a member object with null value. (We didn't give it a value from anywhere, eg = event.getMember())
            	Member user;
            	
            	// If the user only typed userinfo, but nothing else, use the user himself.
            	if (message == null) 
            		user = event.getMember();
            	// If the mentions list of the message is not empty (They mentioned someone) - use the first mentions uer.
            	else if (!event.getMessage().getMentionedUsers().isEmpty())
            		user = event.getGuild().getMember(event.getMessage().getMentionedUsers().get(0));
            	// OTherwise just use our search algo and see if we can find a user.
            	else 
            		user = NUtils.searchUsers(event.getGuild(), message);
            	// if user is equal to null (If they typed something, it wasn't a mention, but it didn't link back to a user.
            	if (user == null) {
            		event.getChannel().sendMessage("I couldn't gind that user").queue();
            		return;
            	}
            	
            	EmbedBuilder embed = new EmbedBuilder();
            	// If the users profle is not equal to null (If they have a profile picture)
            	if (user.getUser().getAvatarUrl() != null)
            		// Add the profile picture as the embed thumbnial
            		embed.setThumbnail(user.getUser().getAvatarUrl());
            	
            	if (user.getNickname() != null)
            		embed.addField("Nickname", user.getNickname(), true);
            	
            	embed.addField("Name", user.getUser().getName(), true);
            	embed.addField("Join data", user.getJoinDate().toString(), true);
            	embed.addField("ID", user.getUser().getId(), true);
            	
            	event.getChannel().sendMessage(NUtils.buildEmbed(embed)).queue();
            	break;
            }
			case "youtube": {
				message = NUtils.removeStart(message);
				
				if (message == null) {
					event.getChannel().sendMessage("Give me something to search, dumbass!").queue();
					return;
				}
				
				/*
				 * The mediao object is not a part of Java, it is part of Utopia. The wrapper you're using.
				 * It contains all information on a video or playlist.
				 */
				
				// Get the information of the rop result
				Media media = Const.YOUTUBE.getVideo(message);
				// If no results were found
				if (media == null) {
					event.getChannel().sendMessage("I couldn't find the shit you wanted.").queue();
					return;
				}
				
				event.getChannel().sendMessage(media.getUrl()).queue();
				break;
			}		
            case "trivia": {
            	message = NUtils.removeStart(message);
		
            	event.getChannel().sendMessage("Sorry. That does not work at the moment.").queue();
		
            	break;
            }
        	case "seth": {
	        	message = NUtils.removeStart(message);
		
	        	event.getChannel().sendMessage("Seth is my teacher. He is a blessing and I'm so happy he takes the time to go through every"
        			+ " meticulous detail with me. I love his silly remarks and how much more fun he makes me day."
        			+ " I hope he lands that coding job. And, I hope he knows that I only push him away at times because"
        			+ " of how busy I think he is. I really just want to steal him away as much as I can. Coding is fun!"
        			+ " I hope he never gives up on me. Yes, Sethy. I'm using a bot to tell my felings. :c"
        			+ " He's so sweet. He said he didn't want this added, but I don't care. He needs to hear this."
        			+ " This project definitely would not have happened. He deserves so much more credit than"
        			+ " he asked me to give him. I love you, Sethy. Please, take care of yourself. **hug**").queue();
		
	        	break;
        	}
            case "leaderboard": {
            	message = NUtils.removeStart(message);
		
            	event.getChannel().sendMessage("Sorry. That does not work at the moment.").queue();
		
            	break;
            }
            case "bunnies": {
            	event.getTextChannel().sendMessage(bing.imageSearch("bunnies", false, true)).queue();
            	break;
            }
            case "level": {
            	message = NUtils.removeStart(message);
		
            	event.getChannel().sendMessage("Sorry. That does not work at the moment.").queue();
		
            	break;
            }
			case "love": {
				
				/*
				 * Do noteice how unlike for the commands above, for this one
				 * we don't call NUtils.removeStart() - this is because
				 * for this one we only needed the first word, none of what
				 * else could be in the message matters. Only use NUtils.removeStart()
				 * when getting arguments. A command like this doesn't have arguments
				 * however.
				 * Grabs a random String from the compliments String array and send it in chat
				 * while replaceing all instance of MENTION with a user mention.
				 */
				
				event.getChannel().sendMessage(compliments[NUtils.rng.nextInt(compliments.length)]
					.replace("<MENTION>", event.getMember().getAsMention())).queue();
				break;
			}
			case "8ball": {
				event.getChannel().sendMessage(":8ball: " + event.getAuthor().getAsMention() +
					" shook the 8ball for an answer...the answer is, " +
						fortune[NUtils.rng.nextInt(fortune.length)] + ":8ball:").queue();
				break;
			}
			case "hug": {
				if (event.getMessage().getMentionedUsers().isEmpty())
					event.getChannel().sendMessage("**hugs** " + event.getAuthor().getAsMention() + " I hope you feel better~.").queue();
				else
					event.getChannel().sendMessage("**hugs** " + event.getMessage().getMentionedUsers().get(0).getAsMention() +
						" " + event.getAuthor().getAsMention() + " just gave you a hug~. ").queue();
				break;
			}
			case "ping": {
				
				/*
				 * In programming because we're all special snowflakes instead of using
				 * a timer like a normal human being, it's apparently easier to get
				 * the number of milliseconds that has passed since 1970 - and then proceed to run
				 * code, and when we finish. Get the number of milliseconds from 1970 again
				 * and deduct the first check, from the 2nd check. The result fo this is the
				 * number of milliseconds that has passed since the first cheeck.
				 */
				
				long timeStamp = System.currentTimeMillis();
				
				/*
				 * In JDA, we do .queue() to let the API know to send the message whenever is
				 * convinient. However the problem with that is though, the message is formed
				 * instantly, so if we put the ping in there, it'd always be 0ms. Since we
				 * are putting the ping in before actually sending the message. So address
				 * this issue we send the message first, and then later append the message
				 * with how long it took to get the message sent via edit.
				 */
				
				event.getChannel().sendMessage("pong!").queue(mes -> {
					mes.editMessage(mes.getContent() + " `" + (System.currentTimeMillis() - timeStamp) + "ms`").queue();
				});
				break;
			}
			case "melons": { 
				event.getChannel().sendMessage("Okay, " + event.getMember().getAsMention() + " I think this evening is over. You've gotta"
					+ " leave. I'll be fine. *You* just have to *go*.").queue();
				break;
			}
			case "repeat": {
				message = NUtils.removeStart(message);
				
				if (!event.isFromType(ChannelType.PRIVATE))
					if (event.getGuild().getSelfMember().hasPermission(event.getTextChannel(), Permission.MESSAGE_MANAGE))
						event.getMessage().delete().queue();
				
				event.getChannel().sendMessage(message).queue();
				break;
			}
			case "help": {
				
				/*
				 * Some objects in programming use builders because it's more flexible
				 * or dynamic for generation rather than passing every possible variable to it
				 * at once. Image if all that down there was added upon creating the Embed object.
				 * That'd be some long ass line. - So we create the Embed Builder and then slwoly
				 * add all the fields and set it up the way we want it.
				 */
				
				EmbedBuilder embed = new EmbedBuilder();
				
				/*
				 * The thumbnail of an embed is the image at the top right.
				 * To remember, if you don't have this set, you can have up to 3 fields
				 * displayed per row, however if you do have a thumbnail
				 * you can only have up to 2 fields per row.
				 */
				
				embed.setThumbnail("https://cdn.discordapp.com/attachments/283116800340787202/293868151530061824/tumblr_oih2uwucVk1viddefo5_250.png");
				embed.setTitle("In-Depth Help page (GitHub Link)", "https://github.com/NazLovesOuma/Ouma-Bot/wiki/Help");
				
				/*
				 * This is how you add a field to an embed, the parameter in the end (True/false)
				 * is if the field can share lines with other fields. If false it will be alone on its
				 * own line.
				 */
				
				embed.addField(Const.PREFIX + "Execute <# to Delete> [@Mention]", "Delete previous messages in the chat.", false);
				embed.addField(Const.PREFIX + "Explain <Body>", "Provide the definition of the word or phrase.", false);
				embed.addField(Const.PREFIX + "Image <Search Term>", "Searches online and returns a random image.", false);
				embed.addField(Const.PREFIX + "Botinfo", "Information on the bot and it's creator.", false);
				embed.addField(Const.PREFIX + "Love", "Feeling down? <(^-^<)", false);
				embed.addField(Const.PREFIX + "Hug", "(>^-^)> \\ (^-^) / <(^-^<)", false);
				
				/*
				 * The footer is the text displayed at the bottom of the embed in small text.
				 * Remember never to hardcode the bots prefix, always refer to Const.PREFIX.
				 */
								
				event.getChannel().sendMessage(NUtils.buildEmbed(embed)).queue();
				break;
			}
			default: {
				// If the message is not from a private channel (Private Messages) - assume it's from a guild
				// and start checking for if they did a guild only command instead.
				if (!event.isFromType(ChannelType.PRIVATE))
					GuildHandler.handle(event, message);				
				break;
			}
		}
	}
}
