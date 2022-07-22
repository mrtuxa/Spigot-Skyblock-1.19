package skyblock.utils.discord.log.core;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import skyblock.utils.discord.log.listener.JoinListener;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bot {

    public static Dotenv dotenv = Dotenv.load();
    public static JDA jda;
    public static List<ListenerAdapter> listeneradapter = new ArrayList<>();

    public static void main(String[] args) {
        String token = dotenv.get("DISCORD_TOKEN");

        List<GatewayIntent> intents = new ArrayList<>(
                Arrays.asList(GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                        GatewayIntent.DIRECT_MESSAGE_TYPING,
                        GatewayIntent.DIRECT_MESSAGES,
                        GatewayIntent.GUILD_BANS,
                        GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
                        GatewayIntent.GUILD_INVITES,
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_MESSAGE_REACTIONS,
                        GatewayIntent.GUILD_MESSAGE_TYPING,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_PRESENCES,
                        GatewayIntent.GUILD_VOICE_STATES,
                        GatewayIntent.GUILD_WEBHOOKS,
                        GatewayIntent.MESSAGE_CONTENT)
        );

        JDABuilder builder = JDABuilder.createDefault(token);

        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.enableCache(CacheFlag.VOICE_STATE);
        builder.enableIntents(intents);
        builder.setActivity(Activity.watching("Diamonds"));

        new JoinListener();

        Listener();

        try {
            jda = builder.build();

            jda.awaitReady();
        } catch (LoginException e) {
            System.err.println("Error: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            System.err.println("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }

        // on stop
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (jda != null) {
                jda.shutdown();
            }
        }));

    }

    private static void Listener() {
        for (ListenerAdapter ad:Bot.listeneradapter) {
            jda.addEventListener(ad);
            System.out.println("Listener added: " + ad.getClass().getName());
        }
    }
}
