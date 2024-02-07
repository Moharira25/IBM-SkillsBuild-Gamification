package com.example.ibm_project_code;

import com.example.ibm_project_code.database.Course;
import com.example.ibm_project_code.repositories.CourseRepository;
import com.example.ibm_project_code.services.CustomUserDetailsService;
import io.github.cdimascio.dotenv.Dotenv;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Objects;

import static io.github.cdimascio.dotenv.Dotenv.*;

@SpringBootApplication(scanBasePackages = "com.example.ibm_project_code")
public class Application implements CommandLineRunner {

    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private CourseRepository courseRepository;

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

        //scraping course-data (college students pathway only) from IBM skills build website.
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
                String link = Objects.requireNonNull(cE.getElementsByClass("flex flex-col md:flex-row").select("a").last()).attr("href");

                assert title != null;
                course.setTitle(title.text());
                course.setDuration(duration.text());
                course.setLanguage(language.text());
                assert description != null;
                course.setDescription(description.text());
                course.setLink(link);
                courseRepository.save(course);
            }
        }

    }
}