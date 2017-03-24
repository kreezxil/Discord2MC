package com.scarabcoder.ereijan.events;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.scarabcoder.ereijan.Main;
import com.scarabcoder.ereijan.ScarabUtil.ScarabUtil;
import com.scarabcoder.ereijan.ScarabUtil.Setting;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter{
	@Override
	public void onMessageReceived(MessageReceivedEvent event)
	{
		if(Main.isAuth){
			if(!event.isPrivate()){
				if(Main.messages.size() > 0){
					if(Main.messages.get(0).getChannelId().equals(event.getMessage().getChannelId())){
						Main.messages.add(0, event.getMessage());
					}


				}
					if(Main.displayMessages){
						
						if(!ScarabUtil.isMuted(event.getTextChannel())){
							String author = event.getAuthorName();
							if(event.getGuild() != null){
								
								
								if(event.getGuild().getNicknameForUser(event.getAuthor()) != null){
									author = event.getGuild().getNicknameForUser(event.getAuthor());
								}
								
								
							}
							String msg = "";
							
							if(ScarabUtil.getSetting(Setting.DISCORD)){
								msg = msg + "[" + ChatFormatting.BLUE + "Discord" + ChatFormatting.RESET + "] "; 
							}
							if(ScarabUtil.getSetting(Setting.SERVER)){
								
								msg = msg + ChatFormatting.RESET + "[" + ChatFormatting.BLUE + event.getGuild().getName() + ChatFormatting.RESET + "] ";
							}
							if(ScarabUtil.getSetting(Setting.CHANNEL)){
								msg = msg + ChatFormatting.RESET + "[" + ChatFormatting.BLUE + "#" + event.getTextChannel().getName() + ChatFormatting.RESET + "] ";
							}
							
							msg = msg + ChatFormatting.GREEN + author + ChatFormatting.RESET + ": " + event.getMessage().getContent();
							
							ScarabUtil.chat(msg);
							Main.last = event.getMessage();
							Main.lastChannel = event.getTextChannel();
						}
					}
				
			}else{
				if(Main.jda.getSelfInfo().getId() != event.getAuthor().getId()){
					ScarabUtil.chat(event.getAuthor().getUsername() + ": " + event.getMessage().getContent());
					Main.last = event.getMessage();
				}
				if(Main.isPrivateMessage){
					Main.messages.add(0, event.getMessage());
				}
				

			}

		}
	}
}
