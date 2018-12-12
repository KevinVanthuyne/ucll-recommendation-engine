package be.ucll.da.recommendation.model;


import be.ucll.da.recommendation.controllers.RecommendedItem;
import be.ucll.da.recommendation.model.Item;
import be.ucll.da.recommendation.model.SlopeOne;
import be.ucll.da.recommendation.model.User;
import be.ucll.da.recommendation.repository.RecommendedItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SlopeOneTest {

    public static UUID Romeinen = UUID.fromString("b4357989-e3b1-42f8-a325-c2591e5c6ad8");
    public static UUID Toverdrank = UUID.fromString("b4357989-e3b1-42f8-a325-c2591e5c6ad9");
    public static UUID Everzwijnen = UUID.fromString("b4357989-e3b1-42f8-a325-c2591e5c6ad7");

    // TODO write a test

    @Before
    private void addDummyRecommendations(@Autowired RecommendedItemRepository repository) {
        HashMap<User, Map<Item, Float>> userMap = new HashMap<>();

        HashMap<Item, Float> asterixPreferences = new HashMap<>();
        asterixPreferences.put(new Item(Toverdrank), 4.8f);
        asterixPreferences.put(new Item(Romeinen), 1.2f);
        asterixPreferences.put(new Item(Everzwijnen), 4.6f);
        userMap.put(new User("0495112233"), asterixPreferences);

        HashMap<Item, Float> obelixPreferences = new HashMap<>();
        obelixPreferences.put(new Item(Toverdrank), 4.4f);
        obelixPreferences.put(new Item(Romeinen), 3.2f);
        obelixPreferences.put(new Item(Everzwijnen), 5f);
        userMap.put(new User("0488200204"), obelixPreferences);

        HashMap<Item, Float> panoramixPreferences = new HashMap<>();
        panoramixPreferences.put(new Item(Toverdrank), 4.2f);
        panoramixPreferences.put(new Item(Romeinen), 2.2f);
        panoramixPreferences.put(new Item(Everzwijnen), 4.1f);
        userMap.put(new User("0472 00 11 69"), panoramixPreferences);

        HashMap<Item, Float> ronaldPreferences = new HashMap<>();
        ronaldPreferences.put(new Item(Toverdrank), 4.9f);
        userMap.put(new User("0455 12 3 koffie"), ronaldPreferences);

        saveUserMapToRepository(repository, userMap);
    }

    @Test
    public void doesSlopeOneDoWhatIExpectItToDo() {
        HashMap<User, Map<Item, Float>> userPreferences = createUserPreferencesMap();


        SlopeOne slopeOne = new SlopeOne(userPreferences);

        HashMap<Item, Float> kakoPhonixPreferences = new HashMap<>();
        kakoPhonixPreferences.put(new Item("Toverdrank"), 4.5f);

        Map<Item, Float> predict = slopeOne.predict(kakoPhonixPreferences);
        System.out.println(predict);
    }

    @Test
    public void newUserDidNotRecommendAnything() {
        HashMap<User, Map<Item, Float>> userPreferences = createUserPreferencesMap();

        SlopeOne slopeOne = new SlopeOne(userPreferences);

        HashMap<Item, Float> kakoPhonixPreferences = new HashMap<>();
        kakoPhonixPreferences.put(new Item("Toverdrank"), 4.1f);
        Map<Item, Float> predict = slopeOne.predict(kakoPhonixPreferences);
        System.out.println(predict);
    }

    private void saveUserMapToRepository(RecommendedItemRepository repository, HashMap<User, Map<Item, Float>> userMap) {
        List<RecommendedItem> recommendedItems = userMap.entrySet().stream()
                .flatMap(entry -> toRecommendedItems(entry))
                .collect(Collectors.toList());

        repository.saveAll(recommendedItems);
    }

    private Stream<RecommendedItem> toRecommendedItems(Map.Entry<User, Map<Item, Float>> entry) {
        List<RecommendedItem> recommendedItems = new ArrayList<>();
        for(Item recommendedItem : entry.getValue().keySet()) {
            RecommendedItem aRecommendedItem = new RecommendedItem();
            aRecommendedItem.setUserId(entry.getKey().toString());
            aRecommendedItem.setRatedItem(UUID.fromString(recommendedItem.toString()));
            aRecommendedItem.setRating(entry.getValue().get(recommendedItem));
            recommendedItems.add(aRecommendedItem);
        }
        return recommendedItems.stream();
    }

    private HashMap<User, Map<Item, Float>> createUserPreferencesMap() {
        HashMap<User, Map<Item, Float>> userMap = new HashMap<>();

        HashMap<Item, Float> asterixPreferences = new HashMap<>();
        asterixPreferences.put(new Item("Toverdrank"), 4.8f);
        asterixPreferences.put(new Item("Romeinen"), 1.2f);
        asterixPreferences.put(new Item("Everzwijnen"), 4.6f);
        userMap.put(new User("Asterix"), asterixPreferences);

        HashMap<Item, Float> obelixPreferences = new HashMap<>();
        obelixPreferences.put(new Item("Toverdrank"), 4.4f);
        obelixPreferences.put(new Item("Romeinen"), 3.2f);
        obelixPreferences.put(new Item("Everzwijnen"), 5f);
        userMap.put(new User("Obelix"), obelixPreferences);

        HashMap<Item, Float> panoramixPreferences = new HashMap<>();
        panoramixPreferences.put(new Item("Toverdrank"), 4.2f);
        panoramixPreferences.put(new Item("Romeinen"), 2.2f);
        panoramixPreferences.put(new Item("Everzwijnen"), 4.1f);
        userMap.put(new User("Panoramix"), panoramixPreferences);

        return userMap;
    }
}
