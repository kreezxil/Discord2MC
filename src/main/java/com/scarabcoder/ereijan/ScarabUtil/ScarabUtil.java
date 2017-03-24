package com.scarabcoder.ereijan.ScarabUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.StringUtils;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.dv8tion.jda.entities.Channel;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

public class ScarabUtil {
	public static void chat(String str){
		Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new TextComponentString(str));
		
	}
	
	public static boolean getSetting(Setting setting){
		List<String> options = new ArrayList<String>(Arrays.asList(ScarabUtil.readFromFile("settings").split(":")));
		switch(setting){
		case DISCORD:
			return Boolean.valueOf(options.get(0));
		case SERVER:
			return Boolean.valueOf(options.get(1));
		case CHANNEL:
			return Boolean.valueOf(options.get(2));
		}
		return false;
	}
	
	public static void setSetting(Setting setting, boolean value){
		String val = value + "";
		List<String> options = new ArrayList<String>(Arrays.asList(ScarabUtil.readFromFile("settings").split(":")));
		switch(setting){
		case DISCORD:
			options.set(0, val);
			break;
		case SERVER:
			options.set(1, val);
			break;
		case CHANNEL:
			options.set(2, val);
			break;
		}
		ScarabUtil.writeToFile("settings", StringUtils.join(options, ":"));
	}
	
	public static boolean isMuted(Channel channel){
		String text = readFromFile("unmuted");
		List<String> channels = new ArrayList<String>(Arrays.asList(text.split(":")));
		for(String str : channels){
			if(str.equalsIgnoreCase(channel.getGuild().getId() + channel.getId())){
				return false;
			}
		}
		return true;
	}
	
	public static void setMuted(Channel channel, boolean mute){
		String text = readFromFile("unmuted");
		List<String> channels = new CopyOnWriteArrayList<String>(Arrays.asList(text.split(":")));
		System.out.println(channel == null);
		if(!mute){
			for(String str : channels){
				
				
				
				if(str.equals(channel.getGuild().getId() + channel.getId())){
					channels.remove(str);
				}
			}
			
			
		}else{
			channels.add(channel.getGuild().getId() + channel.getId());
		}
		writeToFile("unmuted", StringUtils.join(channels, ":"));
	}
	
	public static void writeToFile(String name, String text){
		File file = new File(name);
		if(file.exists()){
			try {
				BufferedWriter writer = Files.newBufferedWriter(file.toPath(), Charset.forName("UTF-8"));
		        writer.write(text, 0, text.length());
		        writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static String readFromFile(String name){
		File file = new File(name);
		StringBuffer stringBuffer = new StringBuffer();

        BufferedReader reader;
		try {
			reader = Files.newBufferedReader(file.toPath(), Charset.forName("UTF-8"));
		

        String line = null;
        while ((line = reader.readLine()) != null) {
            stringBuffer.append(line);
        }

        reader.close();

        return stringBuffer.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
}
