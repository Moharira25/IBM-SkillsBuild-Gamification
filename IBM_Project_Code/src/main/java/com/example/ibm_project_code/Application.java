package com.example.ibm_project_code;

import com.example.ibm_project_code.database.*;
import com.example.ibm_project_code.repositories.*;
import com.example.ibm_project_code.services.CustomUserDetailsService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.time.Instant.now;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ListingRepository listingRepository;
    @Autowired
    private TimeTrialRepository timeTrialRepository;
    @Autowired
    private QuestionsRepository questionsRepository;
    @Autowired
    private UserTrialRepository userTrialRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserItemRepository userItemRepository;

    // loads .env data correctly
/*    static {
        Dotenv dotenv = load();
        dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));
    }*/
    //

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        userDetailsService.addTestUser();

        //A user to test the leaderboard
        User u1 = new User();
        u1.setFirstName("Mohammed");
        u1.setLastName("Abuharira");
        u1.setEmail("mohammed@gmail.com");
        u1.setUsername("maya2");
        u1.setPassword("password");
        u1.setEmailVerified(false);
        u1.setEnabled(true);
        Timestamp currentTime = Timestamp.from(now());
        u1.setCreatedDate(currentTime);
        u1.setLastModifiedDate(currentTime);
        u1.setOverallPoints(100);
        u1.setBalance(BigDecimal.valueOf(1000));
        u1.resetBio();
        userRepository.save(u1);

        //User to show point system working in leaderboard
        User u2 = new User();
        u2.setFirstName("Adam");
        u2.setLastName("Pathan");
        u2.setEmail("ap@outlook.com");
        u2.setUsername("Ap");
        u2.setPassword("pass");
        u2.setEmailVerified(false);
        u2.setEnabled(true);
        Timestamp currentTime2 = Timestamp.from(Instant.now());
        u2.setCreatedDate(currentTime2);
        u2.setLastModifiedDate(currentTime2);
        u2.setOverallPoints(50);
        u2.setBalance(BigDecimal.valueOf(1000));
        u2.resetBio();
        userRepository.save(u2);

        User u3 = new User();
        u3.setFirstName("IBM");
        u3.setLastName("Wizz");
        u3.setEmail("jeff@IBM.com");
        u3.setUsername("IBM");
        u3.setPassword("gradle");
        u3.setEmailVerified(false);
        u3.setEnabled(true);
        Timestamp currentTime3 = Timestamp.from(Instant.now());
        u3.setCreatedDate(currentTime3);
        u3.setLastModifiedDate(currentTime3);
        u3.setOverallPoints(10);
        u3.setBalance(BigDecimal.valueOf(1000));
        u3.resetBio();
        userRepository.save(u3);

        //old trial to show how trials work
        TimeTrial oldTrial = new TimeTrial();
        oldTrial.setEnded(true);
        oldTrial.setParticipants(3);
        oldTrial.setTrialStartTime(LocalDateTime.now().minusDays(7));
        oldTrial.setTitle("Cloud Computing Essentials Trial");
        oldTrial.setDescription("""
                Welcome to the Cloud Computing Essentials Quiz! Test your knowledge about the fundamental concepts and principles of cloud computing with this engaging quiz. Whether you're a beginner exploring the basics or an experienced professional looking to refresh your understanding, this quiz offers an opportunity to challenge yourself and expand your expertise in cloud computing.

                Explore topics such as cloud deployment models, service models, key technologies, and best practices in cloud computing. Each question is designed to cover essential aspects of cloud computing, providing valuable insights into this transformative technology.

                Compete against others and see how well you fare on the leaderboard. Earn recognition for your mastery of cloud computing concepts and take your skills to new heights. Get ready to embark on a journey through the clouds and discover the essentials of cloud computing!""");
        timeTrialRepository.save(oldTrial);
        //creating user trials for three users
        UserTimeTrial ut1 = new UserTimeTrial();
        ut1.setUser(u1);
        ut1.setTimeTrial(oldTrial);
        ut1.setScore(100);
        u1.getTrials().add(ut1);
        oldTrial.getUsersTrials().add(ut1);
        u1.setTrialScore(ut1.getScore());
        userRepository.save(u1);
        userTrialRepository.save(ut1);

        UserTimeTrial ut2 = new UserTimeTrial();
        ut2.setUser(u2);
        ut2.setTimeTrial(oldTrial);
        ut2.setScore(93);
        u2.getTrials().add(ut2);
        u2.setTrialScore(ut2.getScore());
        oldTrial.getUsersTrials().add(ut2);
        userRepository.save(u2);
        userTrialRepository.save(ut2);

        UserTimeTrial ut3 = new UserTimeTrial();
        ut3.setUser(u3);
        ut3.setTimeTrial(oldTrial);
        ut3.setScore(79);
        u3.getTrials().add(ut3);
        u3.setTrialScore(ut3.getScore());
        oldTrial.getUsersTrials().add(ut3);
        userRepository.save(u3);
        userTrialRepository.save(ut3);
        timeTrialRepository.save(oldTrial);

        //creating a time trial for testing
        TimeTrial timeTrial = new TimeTrial();
        timeTrial.setTitle("AI Agents Time Trial Challenge");
        timeTrial.setDescription("Join the AI Agents Time Trial Challenge and put your knowledge of artificial intelligence to the test! " + "Compete weekly against others on a leaderboard for exciting rewards. " + "This week's challenge focuses on AI agents, starting with easy questions and progressively getting harder and harder!. " + "Win coveted prizes such as exclusive avatars, digital badges, profile highlights, and more for top placements. " + "Don't miss out on the chance to showcase your AI expertise and claim your well-deserved rewards!");


        //setting the questions for the time trial.
        String[] questions = {
                "What is an AI agent?",
                "Which of the following is NOT a characteristic of an intelligent agent?",
                "Which type of agent operates in a dynamic environment, taking actions based on the information it receives?",
                "What is the primary function of a utility-based agent?",
                "Which agent type uses internal models to simulate the consequences of potential actions?",
                "What does the term 'sensory perception' refer to in the context of AI agents?",
                "Which agent type adjusts its behavior based on feedback from the environment?",
                "In the context of AI agents, what does autonomy mean?",
                "Which type of agent uses a predefined set of rules and actions to make decisions?",
                "What distinguishes a learning agent from other types of agents?",
                "Which type of agent typically uses a utility function to evaluate the desirability of different states?",
                "What is the role of a performance measure in an AI agent?",
                "Which agent type focuses on maximizing a cumulative utility function over time?",
                "What is the key advantage of a reactive agent?",
                "Which agent type is best suited for environments with well-defined rules and clear objectives?",
                "In the context of AI agents, what does the term \"reactive\" refer to?",
                "Which type of agent focuses on achieving the best possible outcome, regardless of the path taken to get there?",
                "What is the primary challenge faced by reactive agents?",
                "Which agent type is most suitable for dynamic environments where the outcomes of actions are uncertain?",
                "What distinguishes a deliberative agent from other types of agents?",
                "Which type of agent incorporates feedback from the environment to continuously improve its performance?",
                "What role does the environment play in the functioning of an AI agent?",
                "Which agent type typically employs a model-based approach to decision-making?",
                "What distinguishes a utility-based agent from a reactive agent?",
                "Which agent type is most suitable for environments with well-defined rules and objectives, but where outcomes are uncertain?",
                "What is the primary advantage of a reactive agent over other types of agents?",
                "Which agent type relies heavily on predefined rules and actions to guide its behavior?",
                "What distinguishes a learning agent from a utility-based agent?",
                "In the context of AI agents, what does the term \"autonomy\" refer to?",
                "Which agent type focuses on maximizing a cumulative utility function over a period of time?"
        };


        String[] answers = {
                "b) A software program that acts on behalf of a user or another program",
                "d) Fixed behavior",
                "b) Reactive agent",
                "c) To maximize a performance measure based on utility function",
                "c) Deliberative agent",
                "b) The ability to perceive and interpret information from the environment",
                "d) Learning agent",
                "a) The ability to function without human intervention",
                "b) Reactive agent",
                "c) Its ability to improve its performance over time through experience",
                "a) Utility-based agent",
                "a) To evaluate the performance of the agent",
                "a) Utility-based agent",
                "c) Its simplicity and efficiency",
                "c) Deliberative agent",
                "b) A software program that acts on behalf of a user or another program",
                "d) Fixed behavior",
                "a) Utility-based agent",
                "b) To learn from past experiences and improve its performance",
                "d) Learning agent",
                "b) The ability to perceive and interpret information from the environment",
                "d) Learning agent",
                "a) The ability to function without human intervention",
                "b) Its focus on maximizing a performance measure based on utility function",
                "d) Its ability to simulate potential actions before executing them",
                "c) It provides sensory input to the agent",
                "d) Learning agent",
                "c) The ability to understand emotions",
                "c) Deliberative agent",
                "d) Its capability to learn from past experiences"
        };

        String[][] choices = {
                {"a) A person who works with artificial intelligence",
                        "b) A software program that acts on behalf of a user or another program",
                        "c) An organization specializing in AI research",
                        "d) A hardware component used in AI systems"

                },
                {"a) Autonomy",
                        "b) Reactivity",
                        "c) Sensory perception",
                        "d) Fixed behavior"

                },
                {"a) Utility-based agent",
                        "b) Reactive agent",
                        "c) Deliberative agent",
                        "d) Learning agent"

                },
                {"a) To react to stimuli from its environment",
                        "b) To learn from past experiences and improve its performance",
                        "c) To maximize a performance measure based on utility function",
                        "d) To follow a predefined set of rules and actions"

                },
                {"a) Utility-based agent",
                        "b) Reactive agent",
                        "c) Deliberative agent",
                        "d) Learning agent"

                },
                {"a) The ability to understand emotions",
                        "b) The ability to perceive and interpret information from the environment",
                        "c) The ability to communicate with other agents",
                        "d) The ability to learn from past experiences"

                },
                {"a) Utility-based agent",
                        "b) Reactive agent",
                        "c) Deliberative agent",
                        "d) Learning agent"

                },
                {"a) The ability to function without human intervention",
                        "b) The ability to collaborate with other agents",
                        "c) The ability to adapt to changing environments",
                        "d) The ability to perceive and interpret sensory information"

                },
                {"a) Utility-based agent",
                        "b) Reactive agent",
                        "c) Deliberative agent",
                        "d) Learning agent"

                },
                {"a) Its ability to react to stimuli from the environment",
                        "b) Its ability to use utility functions to make decisions",
                        "c) Its ability to improve its performance over time through experience",
                        "d) Its ability to simulate potential actions before executing them"

                },
                {"a) Utility-based agent",
                        "b) Reactive agent",
                        "c) Deliberative agent",
                        "d) Learning agent"

                },
                {"a) To evaluate the performance of the agent",
                        "b) To determine the utility function",
                        "c) To provide sensory input to the agent",
                        "d) To define the environment in which the agent operates"

                },
                {"a) Utility-based agent",
                        "b) Reactive agent",
                        "c) Deliberative agent",
                        "d) Learning agent"

                },
                {"a) Its ability to plan and reason about future actions",
                        "b) Its ability to adapt to changing environments",
                        "c) Its simplicity and efficiency",
                        "d) Its capability to learn from past experiences"

                },
                {"a) Utility-based agent",
                        "b) Reactive agent",
                        "c) Deliberative agent",
                        "d) Learning agent"
                },
                {
                        "a) The ability to plan and reason about future actions",
                        "b) The ability to adapt to changing environments",
                        "c) The ability to take immediate actions based on current stimuli",
                        "d) The ability to learn from past experiences"
                },
                {
                        "a) Utility-based agent",
                        "b) Reactive agent",
                        "c) Deliberative agent",
                        "d) Learning agent"
                },
                {
                        "a) Handling complex environments with uncertain outcomes",
                        "b) Adapting to changing circumstances over time",
                        "c) Dealing with sensory input and reacting appropriately in real-time",
                        "d) Learning from past experiences to improve performance"
                },
                {
                        "a) Utility-based agent",
                        "b) Reactive agent",
                        "c) Deliberative agent",
                        "d) Learning agent"
                },
                {
                        "a) Its ability to react quickly to stimuli from the environment",
                        "b) Its ability to learn from past experiences and adjust its behavior",
                        "c) Its ability to simulate potential actions before making decisions",
                        "d) Its ability to maximize a performance measure based on utility function"
                },
                {
                        "a) Utility-based agent",
                        "b) Reactive agent",
                        "c) Deliberative agent",
                        "d) Learning agent"
                },
                {
                        "a) It provides sensory input to the agent",
                        "b) It determines the agent''s utility function",
                        "c) It evaluates the performance of the agent",
                        "d) It simulates potential actions for the agent to consider"
                },
                {
                        "a) Utility-based agent",
                        "b) Reactive agent",
                        "c) Deliberative agent",
                        "d) Learning agent"
                },
                {
                        "a) Its ability to learn from past experiences",
                        "b) Its focus on maximizing a performance measure based on utility function",
                        "c) Its reliance on immediate sensory input to make decisions",
                        "d) Its simplicity and efficiency in decision-making"
                },
                {
                        "a) Utility-based agent",
                        "b) Reactive agent",
                        "c) Deliberative agent",
                        "d) Learning agent"
                },
                {
                        "a) Its ability to simulate potential actions before making decisions",
                        "b) Its ability to adapt to changing environments over time",
                        "c) Its simplicity and efficiency in decision-making",
                        "d) Its capability to learn from past experiences and improve performance"
                },
                {
                        "a) Utility-based agent",
                        "b) Reactive agent",
                        "c) Deliberative agent",
                        "d) Learning agent"
                },
                {
                        "a) Its ability to react to stimuli from the environment",
                        "b) Its focus on maximizing a performance measure based on utility function",
                        "c) Its capability to improve its performance over time through experience",
                        "d) Its reliance on immediate sensory input to make decisions"
                },
                {
                        "a) The ability to function without human intervention",
                        "b) The ability to collaborate with other agents",
                        "c) The ability to adapt to changing environments",
                        "d) The ability to perceive and interpret sensory information"
                },
                {
                        "a) Utility-based agent",
                        "b) Reactive agent",
                        "c) Deliberative agent",
                        "d) Learning agent"
                }
        };

        for (int i = 0; i < questions.length; i++) {

            Question question = new Question();
            question.setQuestion(questions[i]);
            question.setAnswer(answers[i]);
            question.setChoices(List.of(choices[i]));
            //saving the question to database.
            questionsRepository.save(question);
            //adding the question to the questions list for the time trial.
            timeTrial.getQuestionList().add(question);
        }

        //starting the time trial, i.e. setting the start and end date for it
        timeTrial.Started();
        //Saving the time trial to the database
        timeTrialRepository.save(timeTrial);

        String[] itemTypes = {"Profile Avatars", "Avatar Frames", "Profile Backgrounds", "Customizable Titles", "Chat Bubbles", "Flair", "XP Boosters", "Second attempt for time trials", "Streak Savers", "Timed access to exclusive learning materials", "Collectibles", "Event-specific items", "Anniversary commemorative items"};

        Random random = new Random();
        Item.Rarity[] rarities = Item.Rarity.values();

        String[] adjectives = {"Shimmering", "Pixelated", "Paradoxical", "Whispering", "Neon-Soaked", "Crystalline", "Ephemeral", "Glitch"};
        String[] nouns = {"Echo", "Circuit", "Glitch", "Rune", "Mirage", "Byte", "Paradox", "Whisper"};
        String[] verbs = {"Shifts", "Pulses", "Evolves", "Whispers", "Shatters"};
        String[] frameThemes = {"Fire", "Water", "Celestial", "Floral", "Tech", "Abstract"};
        String[] backgroundThemes = {"Landscape", "Space", "Neon City", "Retro", "Underwater"};
        // multipliers for xp boosters
        String[] xpBoosters = {"1.5x", "2x", "2.5x", "3x", "4x", "5x"};
        // timed access length
        String[] accessLengths = {"1 day", "1 week", "1 month", "3 months", "6 months"};

        int numItemsPerCategory = 5; // Adjust as needed
        Set<String> usedNames = new HashSet<>(); // To track used names

        for (String type : itemTypes) {
            for (int i = 0; i < numItemsPerCategory; ) {
                Item item = new Item();
                String generatedName;

                // Initially assume it's not a default type
                boolean isDefaultType = false;

                do {
                    generatedName = switch (type) {
                        case "Profile Avatars" ->
                                adjectives[random.nextInt(adjectives.length)] + " " + nouns[random.nextInt(nouns.length)];
                        case "Avatar Frames" -> backgroundThemes[random.nextInt(backgroundThemes.length)] + " Frame";
                        case "Profile Backgrounds" -> frameThemes[random.nextInt(frameThemes.length)] + " Background";
                        case "Customizable Titles" ->
                                "The " + adjectives[random.nextInt(adjectives.length)] + " " + nouns[random.nextInt(nouns.length)];
                        case "Chat Bubbles", "Flair" -> type + " of " + adjectives[random.nextInt(adjectives.length)];
                        case "XP Boosters" -> xpBoosters[random.nextInt(xpBoosters.length)] + " XP Booster";
                        case "Timed access to exclusive learning materials" ->
                                accessLengths[random.nextInt(accessLengths.length)] + " Access Pass";
                        case "Collectibles" -> "Collectible " + nouns[random.nextInt(nouns.length)];
                        case "Event-specific items" -> "Event " + verbs[random.nextInt(verbs.length)];
                        case "Anniversary commemorative items" -> "Anniversary " + nouns[random.nextInt(nouns.length)];
                        default -> {
                            isDefaultType = true;
                            yield type; // Use the type directly for the default case
                        }
                    };
                } while (!isDefaultType && !usedNames.add(generatedName)); // Ensure unique names, but skip for default type

                // Populate item details
                item.setName(generatedName);
                item.setDescription("A " + type.toLowerCase() + " to enhance your profile.");
                item.setCategory(type);
                item.setRarity(rarities[random.nextInt(rarities.length)]);
                item.setImageUrl("https://i.pravatar.cc/100"); // Placeholder image URL

                // Save the item
                itemRepository.save(item);

                if (isDefaultType) {
                    break; // Exit the loop after adding one item for default type
                }

                i++; // Increment only if not default type or to proceed to next item
            }
        }

        List<User> users = (List<User>) userRepository.findAll();
        List<Item> items = itemRepository.findAll();
        Random rand = new Random();

        if (!users.isEmpty() && !items.isEmpty()) {
            for (int i = 0; i < 1000; i++) {
                Listing listing = new Listing();
                User seller = users.get(rand.nextInt(users.size()));
                Item item = items.get(rand.nextInt(items.size()));

                listing.setSeller(seller);
                listing.setItem(item);
                // Random price between 0.1 and 20 pounds
                // number should be rounded eg 10.50 not 10.501
                listing.setPrice(BigDecimal.valueOf(rand.nextDouble() * (20 - 0.1) + 0.1).setScale(2, RoundingMode.HALF_UP));
                listing.setStatus(Listing.ListingStatus.ACTIVE); // or randomly set status if you have different statuses

                listingRepository.save(listing);
            }
        } else {
            System.out.println("Users or Items not found. Please ensure your database is populated with users and items before adding listings.");
        }

        List<Listing> listings = listingRepository.findAll(); // Get existing listings

        if (!users.isEmpty() && !items.isEmpty() && !listings.isEmpty()) {
            for (int i = 0; i < 500; i++) { // Adjust number of transactions as needed
                Transaction transaction = new Transaction();

                // Logic to determine buyer and seller (we'll refine this later)
                User buyer = users.get(rand.nextInt(users.size()));
                Listing listing = listings.get(rand.nextInt(listings.size()));
                User seller = listing.getSeller();
                Item item = listing.getItem();
                int saleQuantity = rand.nextInt(3) + 1; // Random quantity between 1 and 3

                // Ensure the seller has the item in user_items with sufficient quantity
                UserItem userItem = userItemRepository.findByUserIdAndItemId(seller.getId(), item.getId());
                if (userItem == null) {
                    userItem = new UserItem();
                    userItem.setUser(seller);
                    userItem.setItem(item);
                    userItem.setQuantity(saleQuantity);
                    userItemRepository.save(userItem);
                } else if (userItem.getQuantity() < saleQuantity) {
                    userItem.setQuantity(userItem.getQuantity() + saleQuantity);
                    userItemRepository.save(userItem);
                }

                // Now the seller is guaranteed to have the item, proceed to create the transaction
                transaction.setBuyer(buyer);
                transaction.setSeller(seller);
                transaction.setItem(item);
                transaction.setPrice(listing.getPrice());
                transaction.setQuantity(saleQuantity);

                // Assuming all transactions result in a sale for simplicity
                transaction.setOrderType(Transaction.OrderType.SELL);
                transaction.setStatus(Transaction.TransactionStatus.PENDING);

                transactionRepository.save(transaction);
            }
        } else {
            System.out.println("Users, Items, or Listings not found. Please ensure your database is populated with users, items, and listings before adding transactions.");
        }


        //scrapping course-data (college students pathway only) from IBM skills build website.
        Document doc = Jsoup.connect("https://skillsbuild.org/college-students/digital-credentials").userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36").get();
        Elements categories = doc.getElementsByClass("pb-12");

        for (Element c : categories) {
            Element category = c.getElementsByClass("badge-section-title bx--productive-heading-06 pt-8 pb-12 max-w-9/10").first();
            Elements categoryElements = c.getElementsByClass("mb-16 bx--row");
            for (Element cE : categoryElements) {

                Course course = new Course();
                assert category != null;
                course.setCategory(category.text());
                Element title = cE.selectFirst(".bx--expressive-heading-03.mb-4");
                Element language = cE.select(".ml-1").get(0);
                Element duration = cE.select(".ml-1").get(2);
                Element description = cE.getElementsByClass("bx--body-long-02 max-w-9/10").select("p").first();
                String link = Objects.requireNonNull(cE.getElementsByClass("flex flex-col md:flex-row").select("a").last()).attr("href");

                // Regular expression to find the numbers in the duration string.
                Pattern pattern = Pattern.compile("\\d+");
                //matching the pattern above to the duration for the course in this iteration.
                Matcher matcher = pattern.matcher(duration.text());
                StringBuilder points = new StringBuilder();
                while (matcher.find()) {
                    points.append(matcher.group());
                }
                course.setPoints(Integer.parseInt(points.toString()) * 2);

                assert title != null;
                course.setTitle(title.text());
                course.setDuration(duration.text());
                course.setLanguage(language.text());
                assert description != null;
                course.setDescription(description.text());
                if (!link.startsWith("/")) {
                    course.setLink(link);
                    courseRepository.save(course);
                }


            }
        }

    }
}