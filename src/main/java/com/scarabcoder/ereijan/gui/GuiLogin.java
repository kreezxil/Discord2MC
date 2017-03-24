package com.scarabcoder.ereijan.gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

import javax.security.auth.login.LoginException;

import com.scarabcoder.ereijan.Main;
import com.scarabcoder.ereijan.ScarabUtil.ScarabUtil;
import com.scarabcoder.ereijan.events.MessageListener;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.client.JDAClientBuilder;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GuiLogin extends GuiScreen{
	
	private GuiTextField text;
	
	private GuiTextField text2;
	
	private String passText = "";
	
	private boolean remember = false;

	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){

        this.drawDefaultBackground();
        
        
        this.text.drawTextBox();
        this.text2.drawTextBox();
        
		this.buttonList.clear();
		
		this.buttonList.add(new GuiButton(1, this.width / 2 - 75, this.height / 4 + 60, 150, 20, "Connect to server"));
		String remember = "No";
		
		if(this.remember){
			remember = "Yes";
		}
		
		this.buttonList.add(new GuiButton(2, 10, 10, 100, 20, "Remember: " + remember));
		this.fontRendererObj.drawString("Email:", this.width / 2 - 135, this.height / 4 + 5, 0xFFFFFF);
		this.fontRendererObj.drawString("Discord Login", this.width / 2 - (fontRendererObj.getStringWidth("Discord Login") / 2), 10, 0xFFFFFF);
		this.fontRendererObj.drawString("Password:", this.width / 2 - 135, this.height / 4 + 35, 0xFFFFFF);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	public void initGui()
    {
        this.text = new GuiTextField(1, this.fontRendererObj, this.width / 2 - 68, this.height/4, 137, 20);
        text.setText("");
        this.text.setFocused(true);
        
        
        this.text2 = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 68, this.height/4 + 30, 137, 20);
        text2.setText("");
        this.text2.setFocused(false);
        
        
        
    }
	
	
	protected void keyTyped(char par1, int par2)
    {
        try {
			super.keyTyped(par1, par2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        this.text.textboxKeyTyped(par1, par2);
        
        if(par1 == '\b'){
        	if(this.text2.isFocused()){
        		if(this.passText.length() != 0){
        			this.passText = this.passText.substring(0, passText.length() - 1);
        		}
        	
        	this.text2.textboxKeyTyped(par1, par2);
        	}
        }else if(text2.isFocused()){
        	if((Character.isLetterOrDigit(par1))){
            	this.text2.textboxKeyTyped('*', par2);
            }
        	if(Character.isLetterOrDigit(par1)){
        		passText = passText + par1;
        	}
        }
        
        
        
        
        if(par1 == '\t'){
        	
        	if(text.isFocused()){
        		text.setFocused(false);
        		text2.setFocused(true);
        	}else if(text2.isFocused()){
        		text2.setFocused(true);
        		text2.setFocused(false);
        	}
        }else if(par1 == '\r'){
        	if(text.isFocused() || text2.isFocused()){
        		connect();
        	}
        }
    }
	
    public void updateScreen()
    {
        super.updateScreen();
        this.text.updateCursorCounter();
        this.text2.updateCursorCounter();
    }
    
    protected void mouseClicked(int x, int y, int btn) {
        try {
			super.mouseClicked(x, y, btn);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        this.text.mouseClicked(x, y, btn);
        
        this.text2.mouseClicked(x, y, btn);
    }
    

	protected void actionPerformed(GuiButton guibutton) {
        //id is the id you give your button
        switch(guibutton.id) {
        case 1:
        	connect();
        	mc.thePlayer.closeScreen();
        	break;
        case 2:
        	this.remember = !remember;
        	break;
        }
	}
	
    private String readFile(File file) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();

        BufferedReader reader = Files.newBufferedReader(file.toPath(), Charset.forName("UTF-8"));

        String line = null;
        while ((line = reader.readLine()) != null) {
            stringBuffer.append(line);
        }

        reader.close();

        return stringBuffer.toString();
    }
	private void connect(){
		
		
		
		try {
			JDA jda = new JDAClientBuilder().setEmail(text.getText()).setAudioEnabled(false).setPassword(passText).buildBlocking();
			
			if(this.remember){
			File file = new File("discord_credentials");
			
			
	        try {
	        	BufferedWriter writer = Files.newBufferedWriter(file.toPath(), Charset.forName("UTF-8"));
				writer.write(text.getText() + ":" + passText, 0, (text.getText() + ":" + passText).length());
		        writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			}
			ScarabUtil.chat("Successfully connected to Discord as " + jda.getSelfInfo().getUsername());
			
			Main.isAuth = true;
			if(Main.isListening){
				Main.jda.removeEventListener(Main.listener);
			}
			MessageListener listener = new MessageListener();
			Main.jda = jda;
			Main.jda.addEventListener(listener);
			Main.listener = listener;
			Main.isListening = true;
		} catch (LoginException e) {
			// TODO Auto-generated catch block
			ScarabUtil.chat("Invalid login credentials!");
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			ScarabUtil.chat("Error, check logs for details.");
			e.printStackTrace();
		} catch (InterruptedException e) {
			ScarabUtil.chat("There was a problem connecting to Discord.");
			e.printStackTrace();
		}
		
	}
}
