package com.example.ibm_project_code;

import com.example.ibm_project_code.database.Course;
import com.example.ibm_project_code.database.User;
import com.example.ibm_project_code.repositories.CourseRepository;
import com.example.ibm_project_code.repositories.UserRepository;
import com.example.ibm_project_code.services.CustomUserDetailsService;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.time.Instant.*;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;

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
        u3.resetBio();
        userRepository.save(u3);


        //scrapping course-data (college students pathway only) from IBM skills build website.
        Document doc = Jsoup
                .connect("https://skillsbuild.org/college-students/digital-credentials")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36")
                .get();
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
                String link = cE.getElementsByClass("flex flex-col md:flex-row").select("a").last().attr("href");

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
                if (!link.startsWith("/")){
                    course.setLink(link);
                    courseRepository.save(course);
                }


            }
        }

    }
}