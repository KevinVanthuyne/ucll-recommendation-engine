package be.ucll.da.recommendation.controllers;

import be.ucll.da.recommendation.model.Item;
import be.ucll.da.recommendation.model.SlopeOne;
import be.ucll.da.recommendation.model.User;
import be.ucll.da.recommendation.repository.RecommendedItemRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;


@RestController
//@CrossOrigin
public class RecommendationController {

    private RecommendedItemRepository repository;

    public RecommendationController(RecommendedItemRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/recommend", method = RequestMethod.POST)
    public RecommendedItem recommendItem(@RequestBody RecommendedItem recommendedItem) {
        return repository.save(recommendedItem);
    }

    @RequestMapping("/recommend/{userId}")
    public Map<Item, Float> getRecommendedItems(@PathVariable String userId) {
        List<RecommendedItem> recommendedItemsByUserId = repository.findAllByUserId(userId);
        System.out.println(recommendedItemsByUserId);

        if(recommendedItemsByUserId.isEmpty()) {
            return null;
        }
        System.out.println("Recommendations not empty for " + userId);

        SlopeOne slopeOnePredictionMachine = getSlopeOnePredictionMachine();
        Map<Item, Float> userPreferences = mapToSlopeOneInput(recommendedItemsByUserId);

        return slopeOnePredictionMachine.predict(userPreferences);
    }

    // why: for testing
    @GetMapping("/recommend/all")
    public Iterable<RecommendedItem> getAllRecommendations() {
        if (repository.findAll() == null) {
            System.out.println("REPOSITORY IS EMPTY");
        }
        return repository.findAll();
    }

    private Map<Item, Float> mapToSlopeOneInput(List<RecommendedItem> recommendedItemsById) {
        HashMap<Item, Float> userPreferences = new HashMap<>();

        for (RecommendedItem recommendedItem : recommendedItemsById) {
            userPreferences.put(new Item(recommendedItem.getRatedItem()), recommendedItem.getRating());
        }
        return userPreferences;
    }

    private SlopeOne getSlopeOnePredictionMachine() {
        Map<User, Map<Item, Float>> allSavedPreferences = new HashMap<>();
        StreamSupport
                .stream(repository.findAll().spliterator(), false)
                .forEach(item -> addRecommendedItemToAllSavedPreferences(allSavedPreferences, item));

        return new SlopeOne(allSavedPreferences);
    }

    private void addRecommendedItemToAllSavedPreferences(Map<User, Map<Item, Float>> allSavedPreferences, RecommendedItem item) {
        User user = new User(item.getUserId());
        if(!allSavedPreferences.containsKey(user)) {
            allSavedPreferences.put(user, new HashMap<>());
        }

        Map<Item, Float> userPrefences = allSavedPreferences.get(user);
        userPrefences.put(new Item(item.getRatedItem()), item.getRating());
    }
}
