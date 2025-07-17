package me.hypericats.hyperionclientv3.auth;

import com.mojang.authlib.minecraft.UserApiService;
import me.hypericats.hyperionclientv3.mixin.MinecraftClientAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.SocialInteractionsManager;
import net.minecraft.client.session.ProfileKeys;
import net.minecraft.client.session.Session;
import net.minecraft.client.session.report.AbuseReportContext;
import net.minecraft.client.session.report.ReporterEnvironment;
import net.minecraft.util.Util;
import net.minecraft.util.Uuids;
import net.minidev.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SessionHandler {
    public static Session getCrackedSession(String name) {
        return getCrackedSession(name, getUUIDFromUsername(name));
    }
    public static Session getCrackedSession(String name, UUID uuid) {
        if (uuid == null) {
            uuid = Uuids.getOfflinePlayerUuid(name);
        }
        return new Session(name, uuid, null, Optional.empty(), Optional.empty(), Session.AccountType.MSA);
    }


    public static Session getFromSessionID(String name, UUID uuid, String sessionID) {
        if (uuid == null) {
            return getCrackedSession(name, null);
        }
        return new Session(name, uuid, sessionID, Optional.empty(), Optional.empty(), Session.AccountType.MSA);
    }

    public static Session getFromSessionID(String name, String sessionID) {
        return getFromSessionID(name, getUUIDFromUsername(name), sessionID);
    }

    // Thanks daddy gpt
    public static UUID getUUIDFromUsername(String username) {
        try {
            String apiUrl = "https://api.mojang.com/users/profiles/minecraft/" + username;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .GET()
                    .build();

            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200 && response.body() != null && !response.body().isEmpty()) {
                    // Manually extract the UUID string using regex
                    Pattern pattern = Pattern.compile("\"id\"\\s*:\\s*\"([a-fA-F0-9]{32})\"");
                    Matcher matcher = pattern.matcher(response.body());

                    if (matcher.find()) {
                        String rawUuid = matcher.group(1);
                        // Insert dashes to make it a valid UUID string
                        String dashedUuid = rawUuid.replaceFirst(
                                "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
                                "$1-$2-$3-$4-$5"
                        );
                        return UUID.fromString(dashedUuid);
                    }
                } else if (response.statusCode() == 204) {
                    System.out.println("Username not found.");
                } else {
                    System.out.println("Error: HTTP " + response.statusCode());
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        } catch (Exception ex) {
            System.err.println("Failed to get UUID for user : " + username);
            return null;
        }
    }

    public static Session getCurrentSession() {
        return MinecraftClient.getInstance().getSession();
    }

    public static void setSession(Session session) {
        MinecraftClientAccessor mca = (MinecraftClientAccessor) MinecraftClient.getInstance();
        mca.setSession(session);
        UserApiService apiService;
        apiService = mca.getAuthenticationService().createUserApiService(session.getAccessToken());
        mca.setUserApiService(apiService);
        mca.setSocialInteractionsManager(new SocialInteractionsManager(MinecraftClient.getInstance(), apiService));
        mca.setProfileKeys(ProfileKeys.create(apiService, session, MinecraftClient.getInstance().runDirectory.toPath()));
        mca.setAbuseReportContext(AbuseReportContext.create(ReporterEnvironment.ofIntegratedServer(), apiService));
        mca.setGameProfileFuture(CompletableFuture.supplyAsync(() -> MinecraftClient.getInstance().getSessionService().fetchProfile(MinecraftClient.getInstance().getSession().getUuidOrNull(), true), Util.getIoWorkerExecutor()));
    }
}
