package io.reactivestax.service;

import io.reactivestax.repository.entity.RuleSet;
import io.reactivestax.utility.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class TreeProcessor {

    private final Stack<RuleSet> nodeProcess = new Stack<>();
    private final AtomicInteger atomicCounter = new AtomicInteger();
    private final List<RuleSet> ruleSetList = new ArrayList<>();

    public void applyRuleSet(List<String> ruleValueSet) {
        for (String dataSet : ruleValueSet) {
            String key = dataSet.substring(0, 2);
            String value = dataSet.substring(2);
            if (isTerminalNode(key)) {
                if (!nodeProcess.isEmpty() && Objects.equals(key, "07")) { //since 7 is special case here we have to pop the element from the stack but we don't have to push in stack since it is terminal node push in list
                    while (nodeProcess.peek().getRuleType().equals("04"))
                        popAndAddToList();
                }
                RuleSet build = getRuleSet(key, value);
                ruleSetList.add(build);
            } else {
                if (!nodeProcess.isEmpty() && (Objects.equals(key, nodeProcess.peek().getRuleType()) || Integer.parseInt(key) < Integer.parseInt(nodeProcess.peek().getRuleType()))) {
                    while (Integer.parseInt(nodeProcess.peek().getRuleType()) > Integer.parseInt(key)) {
                        popAndAddToList();
                    }
                    popAndAddToList();
                    buildStackElement(key, value);
                } else {
                    buildStackElement(key, value);
                }
            }
        }
        processRemainingStackElement();
        log.info("stack trace {}", nodeProcess);
        log.info("Element size {}", ruleSetList.size());
        insertIntoDb();
    }

    private void processRemainingStackElement() {
        while (!nodeProcess.isEmpty()) {
            popAndAddToList();
        }
    }

    private void insertIntoDb() {
        List<RuleSet> collect = ruleSetList.stream().sorted(Comparator.comparing(RuleSet::getLeftNode)).toList();
        Session session = HibernateUtil.getInstance().getConnection();
        session.beginTransaction();
        collect.forEach(session::persist);
        session.getTransaction().commit();
    }

    private void popAndAddToList() {
        RuleSet popElement = nodeProcess.pop();
        popElement.setRightNode(atomicCounter.incrementAndGet());
        ruleSetList.add(popElement);
    }

    private RuleSet getRuleSet(String key, String value) {
        return RuleSet.builder()
                .leftNode(atomicCounter.incrementAndGet())
                .rightNode(atomicCounter.incrementAndGet())
                .ruleType(key)
                .value(value)
                .build();
    }

    private void buildStackElement(String key, String value) {
        RuleSet tempData = RuleSet.builder()
                .leftNode(atomicCounter.incrementAndGet())
                .rightNode(-1)
                .ruleType(key)
                .value(value)
                .build();
        nodeProcess.push(tempData);
    }

    public boolean isTerminalNode(String key) {
        return Objects.equals(key, "02") || Objects.equals(key, "05") || Objects.equals(key, "06") || Objects.equals(key, "07");
    }
}
