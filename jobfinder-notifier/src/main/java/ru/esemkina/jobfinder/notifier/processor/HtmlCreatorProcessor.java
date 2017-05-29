package ru.esemkina.jobfinder.notifier.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import ru.esemkina.jobfinder.web.ui.simple.entity.Client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kate on 28.05.2017.
 */
public class HtmlCreatorProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        HashMap body = exchange.getIn().getBody(HashMap.class);
        Client client = (Client) body.get("client");
        HashMap vacanciesMap = (HashMap) body.get("vacancies");
        List<Map> vacancies = (List) vacanciesMap.get("vacancies");
        String vacanciesTemplate = exchange.getProperty("vacanciesTemplate").toString();
        String vacancyTemplate = exchange.getProperty("vacancyTemplate").toString();

        exchange.setProperty("client", client);
        exchange.getOut().setBody(produceHtml(vacanciesTemplate, vacancyTemplate, client, vacancies));
    }

    private String produceHtml(String vacanciesTemplate, String vacancyTemplate, Client client, List<Map> vacancies) {
        String template = vacanciesTemplate.replaceAll("%name%", client.getName());

        StringBuilder listOfVacanciesTemplate = new StringBuilder();
        for (Map x : vacancies) {
            String item = vacancyTemplate;
            item = item.replaceAll("%url%", String.valueOf(x.get("url")));
            item = item.replaceAll("%name%", String.valueOf(x.get("name")));
            item = item.replaceAll("%responsibility%", String.valueOf(x.get("responsibility")));
            item = item.replaceAll("%requirement%", String.valueOf(x.get("requirement")));
            item = item.replaceAll("%employerName%", String.valueOf(x.get("employerName")));
            item = item.replaceAll("%createdAt%", String.valueOf(x.get("createdAt")));
            item = item.replaceAll("%city%", String.valueOf(x.get("city")));
            listOfVacanciesTemplate.append(item);
        }

        return template.replaceAll("%vacansies%", listOfVacanciesTemplate.toString());
    }
}
