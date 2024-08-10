package com.example.jobscraper.scraper;

import com.example.jobscraper.model.Job;
import com.example.jobscraper.service.JobService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JobScraper {

    @Autowired
    private JobService jobService;

    @Scheduled(fixedRate = 60000) // Scrape every minute (adjust as needed)
    public void scrapeJobs() throws Exception {
        String url = "https://beyondkareers.com/app/jobs";
        Document document = Jsoup.connect(url).get();

        Elements jobElements = document.select(".job-listing"); // Adjust the selector based on the actual HTML structure

        for (Element jobElement : jobElements) {
            Job job = new Job();
            job.setJobTitle(jobElement.select(".job-title").text());
            job.setExperience(jobElement.select(".experience").text());
            job.setJobDescription(jobElement.select(".job-description").text());
            job.setPackageAmount(jobElement.select(".package").text());
            job.setSkills(jobElement.select(".skills").text());
            job.setLocation(jobElement.select(".location").text());
            job.setContactPerson(jobElement.select(".contact-person").text());
            job.setCompanyName(jobElement.select(".company-name").text());
            job.setCompanyLogo(jobElement.select(".company-logo").attr("src"));

            jobService.saveJob(job);
        }
    }
}
