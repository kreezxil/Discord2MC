package com.scarabcoder.ereijan;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.security.auth.login.LoginException;

import com.scarabcoder.ereijan.ScarabUtil.ScarabUtil;
import com.scarabcoder.ereijan.ScarabUtil.Strings;
import com.scarabcoder.ereijan.events.Events;
import com.scarabcoder.ereijan.events.MessageListener;
import com.scarabcoder.ereijan.proxy.CommonProxy;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.client.JDAClient;
import net.dv8tion.jda.client.JDAClientBuilder;
import net.dv8tion.jda.entities.Channel;
import net.dv8tion.jda.entities.Message;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Strings.id, name = Strings.name, version = Strings.version)
public class Main {
	@SidedProxy(clientSide = Strings.clientProxyClass, serverSide = Strings.commonProxyClass)
	public static CommonProxy proxy;
	
	public static boolean showChat = true;
	
	public static boolean isAuth = false;
	
	public static Message last;
	
	public static Channel lastChannel;
	
	public static boolean isListening = false;
	
	public static MessageListener listener = new MessageListener();
	
	public static JDA jda;
	
	public static List<Message> messages = new CopyOnWriteArrayList<Message>();
	
	public static boolean displayMessages = true;
	
	public static boolean isPrivateMessage = false;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		
		MinecraftForge.EVENT_BUS.register(new Events());
        Keybinds.init();
	}
	
	public static String readFile(File file) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();

        BufferedReader reader = Files.newBufferedReader(file.toPath(), Charset.forName("UTF-8"));

        String line = null;
        while ((line = reader.readLine()) != null) {
            stringBuffer.append(line);
        }

        reader.close();

        return stringBuffer.toString();
    }
	
	
	
	@EventHandler
	public void init(FMLInitializationEvent event){
		File file2 = new File("unmuted");
		if(!file2.exists()){
			try {
				file2.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		File settings = new File("settings");
		if(!settings.exists()){
			try {
				settings.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			ScarabUtil.writeToFile("settings", "true:true:true");
		}
		
		File file = new File("discord_credentials");
		if(!file.exists()){
			try {
				file.createNewFile();
				BufferedWriter writer = Files.newBufferedWriter(file.toPath(), Charset.forName("UTF-8"));
		        writer.write("null:null", 0, "null:null".length());
		        writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			try {
				String cred = readFile(file);
				String[] creds = cred.split(":");
				if(!creds[0].equalsIgnoreCase("null")){
					try {
						JDAClient jda = new JDAClientBuilder().setEmail(creds[0]).setPassword(creds[1]).buildBlocking();
						
						
						
						
						
						
						Main.isAuth = true;
						if(Main.isListening){
							Main.jda.removeEventListener(this.listener);
						}
						Main.jda = jda;
						MessageListener listener = new MessageListener();
						Main.jda.addEventListener(listener);
						Main.listener = listener;
					} catch (LoginException e) {
						System.out.println("Error loading credentials from file, invalid email/password.");
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						System.out.println("Error loading credentials from file.");
						e.printStackTrace();
					} catch (InterruptedException e) {
						System.out.println("Could not connect to Discord servers for authentication from file.");
						e.printStackTrace();
					}
				}else{
					BufferedWriter writer = Files.newBufferedWriter(file.toPath(), Charset.forName("UTF-8"));
			        writer.write("null:null", 0, "null:null".length());
			        writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
    

	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		
	}
}
