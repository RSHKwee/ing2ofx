package zandbak;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

public class GitHubUpdateChecker {
  public static void main(String[] args) {
    String owner = "RSHKwee";
    String repoName = "ing2ofx";
    String currentVersion = "v0.1.0.0"; // Replace with your current version or commit hash

    String latestTag = getGitHubReleases(owner, repoName);
    if (isUpdateAvailable(currentVersion, latestTag)) {
      System.out.println("An update is available! Latest version: " + latestTag);
    } else {
      System.out.println("Your application is up to date.");
    }

    repoName = "garminsummary";
    latestTag = getGitHubReleases(owner, repoName);
    if (isUpdateAvailable(currentVersion, latestTag)) {
      System.out.println("An update is available! Latest version: " + latestTag);
    } else if (!latestTag.isBlank()) {
      System.out.println("Your application is up to date.");
    } else {
      System.out.println("No releases found for " + repoName);
    }

    repoName = "replaceTool";
    latestTag = getGitHubReleases(owner, repoName);
    if (isUpdateAvailable(currentVersion, latestTag)) {
      System.out.println("An update is available! Latest version: " + latestTag);
    } else if (!latestTag.isBlank()) {
      System.out.println("Your application is up to date.");
    } else {
      System.out.println("No releases found for " + repoName);
    }

  }

  private static String getGitHubReleases(String owner, String repoName) {
    String latestTag = "";
    try {
      URL url = new URL("https://api.github.com/repos/" + owner + "/" + repoName + "/releases/latest");
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.setRequestProperty("Accept", "application/vnd.github.v3+json");
      connection.connect();

      int responseCode = connection.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_OK) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
          response.append(line);
        }
        reader.close();

        latestTag = response.toString();
        String[] elems = latestTag.split(",");

        for (int i = 0; i < elems.length; i++) {
          if (elems[i].contains("releases/tag")) {
            System.out.println("tag: " + elems[i]);
            String[] elrel = elems[i].split("/");
            String version = elrel[elrel.length - 1];
            version = version.replace("\"", "");
            System.out.println("Version: " + version);
            latestTag = version;
          }
        }
        System.out.println("Repo: " + repoName + " version " + latestTag);
      } else {
        System.out.println("Failed to fetch the latest release information. Response code: " + responseCode);
      }
      connection.disconnect();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return latestTag;
  }

  private static boolean isUpdateAvailable(String currentVersion, String latestTag) {
    // Implement your logic to compare versions here
    // For example, you can use semantic versioning comparison logic
    return currentVersion.compareToIgnoreCase(latestTag) < 0;
  }
}
