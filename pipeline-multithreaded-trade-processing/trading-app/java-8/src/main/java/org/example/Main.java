package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class Main {


    @Data
    @AllArgsConstructor
    public static class Student {
        String name;
        int age;
        String address;
        String level;
    }


    //this is consumer
    private static void printStudent(Student student) {
        log.info("student Name " + student.name);
        log.info("student age " + student.age);
        log.info("student Address " + student.address);
        log.info("student level " + student.level);
    }

    private static void useOfFlatMapJavaEight() {
        List<List<String>> cityList = Arrays.asList(
                Arrays.asList(
                        "Toronto", "NorthYork", "Mississauga", "Brampton"
                )
        );

        List<String> collect = cityList.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

//        cityList.stream()
//                        .filter(city -> city.stream().filter(c -> c.startsWith("T"))
//                                .collect(Collectors.toList());

        System.out.println("collect: " + collect);

        System.out.println("Nested without Flat-map " + cityList);

    }

    public static void processStudents(
            List<Student> students,
            Predicate<Student> checker,
            Consumer<Student> consumer) {
        for (Student student : students) {
            if (checker.test(student)) {
                consumer.accept(student);
            }
        }
    }

    public static void processStudentWithFunction(
            List<Student> students,
            Predicate<Student> tester,
            Function<Student, String> mapper,
            Consumer<String> logger) {
        for (Student student : students) {
            if (tester.test(student)) {
                String apply = mapper.apply(student);
                logger.accept(apply);
            }
        }
    }


    public static void main(String[] args) {

//        List<String> list = Arrays.asList("Java8", "kotlin", "Scala");
//        Stream<String> stream = list.stream();
//        stream.forEach(System.out::println);
//        stream.forEach(System.out::println);

        String[] words = {"Hello", "World"}; // this is normal list........

//        List<String[]> collect = Arrays.stream(words).
//                map(w -> w.split(""))
//                .collect(Collectors.toList());


        Arrays.stream(words)
                .map(w -> w.split(""))
                .flatMap(Arrays::stream)
                .distinct()
                .skip(1)
                .forEach(System.out::println);


        Integer numbers[] = {5, 6, 7, 12, 10};

        Stream<Integer> integerStream = Arrays.stream(numbers);
        Integer reduce = integerStream.
                skip(1)
                .filter(i -> i > 0)
                .map(i -> i + 1)
                .reduce(0, (a,b)-> a-b);
        System.out.println("reduce" + reduce);




        Student student1 = new Student("suraj", 18, "North-york", "Java7");
        Student student2 = new Student("rocker", 22, "West-york", "Java8");
        Student student3 = new Student("suad", 20, "Yeast-york", "Java17");

        List<Student> studentList = Arrays.asList(student1, student2, student3);

        if(studentList.stream().anyMatch(s-> s.age > 18)){
            System.out.println("Eligible for alcohol buying at LCBO");
        }

 studentList.stream()
                .filter(s -> s.address.startsWith("N"))
                .findAny()
                .ifPresent(System.out::println);

        boolean is = studentList.stream().allMatch(s -> s.age > 18);


        //generating the stream using Stream.iterate()

        Stream<Integer> iterate = Stream.iterate(0, i -> i + 2).limit(10);
        System.out.println((iterate.count()));
        iterate.forEach(System.out::println); // we cannot consume the stream more than once


//
//        studentList.stream()
//                .filter(student -> student.age > 10)
//                .limit(2)
////                .skip(1)
//                .forEach(System.out::println);

//        processStudents(studentList,
//                student -> student.getAge() >= 20,
//                System.out::println
//        );
//
//        processStudentWithFunction(studentList,
//                student -> student.getAge() < 20,
//                student -> student.getName(),
//                System.out::println
//        );
//
//
//
//
//        studentList.stream()
//                .filter(student -> student.age > 19)
//                .map(student -> new Student(student.getName().toUpperCase(), student.getAge(), student.getAddress().toUpperCase(), student.getLevel().toUpperCase()))
//                .forEach(s -> Main.printStudent(s));
//
//        useOfFlatMapJavaEight();
    }

    private static int sum(int a , int b){
        return a+b;
    }



}