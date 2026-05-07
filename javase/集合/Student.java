package src.main.java.javase.集合;

public class Student implements Comparable<Student> {
    private Integer id;
    private String name;
    private Integer age;
    private Double score;

    // 构造器
    public Student(Integer id, String name, Integer age, Double score) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.score = score;
    }

    // getter & setter
    public Integer getId() { return id; }
    public String getName() { return name; }
    public Integer getAge() { return age; }
    public Double getScore() { return score; }

    // 重写toString
    @Override
    public String toString() {
        return "Student{id=" + id + ", name='" + name + "', age=" + age + ", score=" + score + "}";
    }

    // 重写equals和hashCode 用于Set去重
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id.equals(student.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    // 实现Comparable：TreeSet按年龄升序
    @Override
    public int compareTo(Student o) {
        return this.age - o.age;
    }
}
