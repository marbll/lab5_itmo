package Labs;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import javax.management.modelmbean.XMLParseException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import java.io.*;
import java.time.*;
import java.util.*;

/**
 * Содержит команды, управляющие коллекцией
 */
public class CollectionManager {
    private PriorityQueue<LabWork> labWorks;
    protected static ArrayList<String> scripts = new ArrayList<>();
    private String collectionPath;
    private File xmlCollection;
    private Date initDate;
    private Iterator iterator;
    private boolean wasStart;
    private Integer globalId;
    private LocalDate globalCreationDate;
    protected static HashMap<String, String> manual;

    {
        labWorks = new PriorityQueue<>();
        iterator = labWorks.iterator();
        globalId = null;
        globalCreationDate = null;
        manual = new HashMap<>();
        manual.put("help","вывести справку по доступным командам");
        manual.put("info","вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)");
        manual.put("show","вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        manual.put("add {element}","добавить новый элемент в коллекцию");
        manual.put("update id {element}","обновить значение элемента коллекции, id которого равен заданному");
        manual.put("remove_by_id id","удалить элемент из коллекции по его id");
        manual.put("clear","очистить коллекцию");
        manual.put("save","сохранить коллекцию в файл");
        manual.put("execute_script file_name","считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме");
        manual.put("exit","завершить программу (без сохранения в файл)");
        manual.put("remove_first"," удалить первый элемент из коллекции");
        manual.put("remove_greater {element}"," удалить из коллекции все элементы, превышающие заданный");
        manual.put("history","вывести последние 8 команд (без их аргументов)");
        manual.put("average_of_average_point"," вывести среднее значение поля averagePoint для всех элементов коллекции");
        manual.put("filter_by_average_point averagePoint"," вывести элементы, значение поля averagePoint которых равно заданному");
        manual.put("print_field_descending_average_point"," вывести значения поля averagePoint всех элементов в порядке убывания");
    }

    public CollectionManager(String collectionPath)  {
        try {
            File file = new File(collectionPath);
            if (file.exists()) {
                this.xmlCollection = file;
                this.collectionPath = collectionPath;
            }
            else throw new FileNotFoundException();
        } catch (FileNotFoundException ex) {
            System.out.println("Путь до файла xml нужно передать через переменную окружения. Файл по указанному пути не существует.");
            System.exit(1);
        }
        this.load();
        this.initDate = new Date();
        wasStart = true;
    }


    /**
     * Выводит на экран список доступных для пользователя команд
     */
    public void help() {
        System.out.println("Доступные к использованию команды:");
        manual.keySet().forEach(p -> System.out.println(p + " - " + manual.get(p)));
    }

    /**
     * Выводит все элементы коллекции
     */
    public void show() {
        if (labWorks.size() != 0) {
            labWorks.forEach(System.out::println);
        }
        else System.out.println("В коллекции отсутствуют элементы. Выполнение команды невозможно.");
    }

    /**
     * Обрабатывает ввод нового элемента
     */
    public LabWork newLabWork() {
        Scanner reader = new Scanner(System.in);
        System.out.print("Введите name: ");
        String name = reader.nextLine();
        while (name.equals("")) {
            System.out.println("Поле не может быть null или пустой строкой ");
            System.out.print("Введите name: ");
            name = reader.nextLine();
        }
        System.out.println("Введите coordinates: ");
        String a;
        boolean p = false;
        int x = 0;
        while (!p) {
            System.out.print("Введите x: ");
            a = reader.nextLine();
            try {
                x = Integer.parseInt(a);
                if (x > -985)
                    p = true;
                else {
                    System.out.println("Значение поля должно быть больше -985");
                }
            } catch (NumberFormatException e) {
                System.out.println("Аргумент не является значением типа int");
            }
        }
        Double y = null;
        while (p) {
            System.out.print("Введите y: ");
            a = reader.nextLine();
            try {
                y = Double.parseDouble(a);
                p = false;
            } catch (NumberFormatException e) {
                System.out.println("Аргумент не является значением типа Double");
            }
        }
        long minimalPoint = 0;
        while (!p) {
            System.out.print("Введите minimalPoint: ");
            a = reader.nextLine();
            try {
                minimalPoint = Long.parseLong(a);
                if (minimalPoint > 0)
                    p = true;
                else {
                    System.out.println("Значение поля должно быть больше 0");
                }
            } catch (NumberFormatException e) {
                System.out.println("Аргумент не является значением типа long");
            }
        }
        System.out.print("Введите description: ");
        String description = reader.nextLine();
        while (description.equals("")) {
            System.out.println("Поле не может быть null или пустой строкой ");
            System.out.print("Введите description: ");
            description = reader.nextLine();
        }
        Double averagePoint = null;
        while (p) {
            System.out.print("Введите averagePoint: ");
            a = reader.nextLine();
            try {
                averagePoint = Double.parseDouble(a);
                if (averagePoint > 0)
                    p = false;
                else {
                    System.out.println("Значение поля должно быть больше 0");
                }
            } catch (NumberFormatException e) {
                System.out.println("Аргумент не является значением типа Double");
            }
        }
        System.out.print("Введите Difficulty (VERY_EASY, EASY, NORMAL, INSANE, null): ");
        String difficulty_s = reader.nextLine();
        while (!difficulty_s.equals("") && !difficulty_s.equals("VERY_EASY") && !difficulty_s.equals("EASY") && !difficulty_s.equals("NORMAL") && !difficulty_s.equals("INSANE")) {
            System.out.println("Значение поля неверное");
            System.out.print("Введите Difficulty (VERY_EASY, EASY, NORMAL, INSANE, null): ");
            difficulty_s = reader.nextLine();
        }
        Difficulty difficulty = null;
        if (!difficulty_s.equals("")) difficulty = Difficulty.valueOf(difficulty_s);
        System.out.println("Введите Discipline: ");
        System.out.print("Введите name: ");
        String nameDiscipline = reader.nextLine();
        while (nameDiscipline.equals("")) {
            System.out.println("Поле не может быть null");
            System.out.print("Введите name: ");
            nameDiscipline = reader.nextLine();
        }
        Long labsCount = null;
        while (!p) {
            System.out.print("Введите labsCount: ");
            a = reader.nextLine();
            try {
                if (a.equals("")) p = true;
                else {
                    labsCount = Long.parseLong(a);
                    p = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Аргумент не является значением типа Long");
            }
        }

        Integer id = null;
        if (globalId == null) {
            while (p) {
                p = false;
                Random random = new Random();
                id = random.nextInt(10000) + 1;
                iterator = labWorks.iterator();
                while (iterator.hasNext()) {
                    LabWork l = (LabWork) iterator.next();
                    if (l.getId().equals(id)) {
                        p = true;
                        break;
                    }
                }
            }
        } else id = globalId;

        LocalDate creationDate;
        if (globalCreationDate == null) creationDate = LocalDate.now();
        else creationDate = globalCreationDate;
        System.out.println("Все значения элемента успешно получены");
        return new LabWork(id, name, new Coordinates(x, y), creationDate, minimalPoint, description, averagePoint, difficulty, new Discipline(nameDiscipline, labsCount));
    }


    /**
     * Добавляет новый элемент в коллекцию=
     */
    public void add() {
        labWorks.add(newLabWork());
        System.out.println("Элемент успешно добавлен");
    }

    /**
     * Обновляет значение элемента коллекции, id которого равен заданному
     * @param n : Id элемента, который требуется заменить
     */
    public void update(String n){
        if (labWorks.size() != 0) {
            try {
                Integer id = Integer.valueOf(n);
                boolean b = false;
                iterator = labWorks.iterator();
                globalId = id;
                while (iterator.hasNext()) {
                    LabWork l = (LabWork) iterator.next();
                    if (l.getId().equals(id)) {
                        globalCreationDate = l.getCreationDate();
                        labWorks.remove(l);
                        labWorks.add(newLabWork());
                        System.out.println("Элемент коллекции успешно обновлен.");
                        b = true;
                        break;
                    }
                }
                globalId = null;
                globalCreationDate = null;
                if (!b) System.out.println("В коллекции не найдено элемента с указанным id.");
            } catch (NumberFormatException ex) {
                System.out.println("Аргумент не является значением типа Integer");
            }
        } else System.out.println("В коллекции отсутствуют элементы. Выполнение команды не возможно.");

    }

    /**
     * Удаляет элемент из коллекции по его id
     * @param n :id соответствующего элемента, который требуется удалить
     */
    public void remove_by_id(String n){
        if (labWorks.size() != 0) {
            try {
                boolean b = false;
                Integer id = Integer.parseInt(n);
                iterator = labWorks.iterator();
                while (iterator.hasNext()) {
                    LabWork l = (LabWork) iterator.next();
                    if (l.getId().equals(id)) {
                        labWorks.remove(l);
                        System.out.println("Элемент коллекции успешно удален.");
                        b = true;
                        break;
                    }
                }
                if (!b) System.out.println("В коллекции не найдено элемента с указанным ключом.");
                else System.out.println("Команда успешно выполнена.");
            } catch (NumberFormatException e) {
                System.out.println("Аргумент не является значением типа Integer");
            }
        }
        else System.out.println("В коллекции отсутствуют элементы. Выполнение команды не возможно.");
    }

    /**
     * Удаляет все элементы коллекции.
     */
    public void clear() {
        labWorks.clear();
        System.out.println("Коллекция очищена.");
    }

    /**
     * Сериализует коллекцию в файл json.
     */
    public void save() {
        try  {
            Document doc = new Document();
            // создаем корневой элемент с пространством имен
            doc.setRootElement(new Element("Labs"));
            // формируем JDOM документ из объектов Student
            iterator = labWorks.iterator();
            while (iterator.hasNext()) {
                LabWork labWork = (LabWork) iterator.next();
                Element element = new Element("LabWork");
                element.addContent(new Element("id").setText( String.valueOf(labWork.getId())));
                element.addContent(new Element("name").setText(labWork.getName()));
                Element element_c = new Element("Coordinates");
                element_c.addContent(new Element("x").setText(String.valueOf(labWork.getCoordinates().getX())));
                element_c.addContent(new Element("y").setText(String.valueOf(labWork.getCoordinates().getY())));
                element.addContent(element_c);
                element.addContent(new Element("creationDate").setText(String.valueOf(labWork.getCreationDate())));
                element.addContent(new Element("minimalPoint").setText(String.valueOf(labWork.getMinimalPoint())));
                element.addContent(new Element("description").setText(labWork.getDescription()));
                element.addContent(new Element("averagePoint").setText(String.valueOf(labWork.getAveragePoint())));
                element.addContent(new Element("difficulty").setText(String.valueOf(labWork.getDifficulty())));
                Element element_d = new Element("Discipline");
                element_d.addContent(new Element("name").setText(labWork.getDiscipline().getName()));
                element_d.addContent(new Element("labsCount").setText(String.valueOf(labWork.getDiscipline().getLabsCount())));
                element.addContent(element_d);
                doc.getRootElement().addContent(element);
            }
            if (!xmlCollection.canWrite()) throw new SecurityException();
            // Документ JDOM сформирован и готов к записи в файл
            XMLOutputter xmlWriter = new XMLOutputter(Format.getPrettyFormat());
            // сохнаряем в файл
            xmlWriter.output(doc, new FileOutputStream(xmlCollection));
            System.out.println("Коллекция успешно сохранена в файл.");
        } catch (IOException ex) {
            System.out.println("Возникла непредвиденная ошибка. Коллекция не может быть записана в файл");
        } catch (SecurityException ex) {
            System.out.println("Файл защищён от записи. Невозможно сохранить коллекцию.");
        }
    }

    /**
     * Считывает и исполняет скрипт из указанного файла.
     * В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме
     */
    public void execute_script(String file, ArrayList<String> commands_of_script) {
        if (scripts.contains(file)) {
            Commander.last_commands.remove(Commander.last_commands.size() - 1);
            System.out.println("Могло произойти зацикливание при исполнении скрипта: " + file + "\nКоманда не будет выполнена. Переход к следующей команде");
        } else {
            File file1 = new File(file);
            if (!file1.exists())
                System.out.println("Файла с таким названием не существует.");
            else if (!file1.canRead())
                System.out.println("Файл защищён от чтения. Невозможно выполнить скрипт.");
            else {
                scripts.add(file);
                try (InputStreamReader commandReader = new InputStreamReader(new FileInputStream(file1))) {
                    StringBuilder s = new StringBuilder();
                    while (commandReader.ready()) s.append((char) commandReader.read());
                    String[] s1 = s.toString().split("\n");
                    commands_of_script.addAll(Arrays.asList(s1));
                } catch (IOException ex) {
                    System.out.println("Невозможно считать скрипт");
                    scripts.remove(scripts.size()-1);
                }
            }
        }
    }

    /**
     * Удаляет из коллекции все элементы, превышающие заданный
     */
    public void remove_greater() {
        if (labWorks.size() != 0) {
            LabWork l = newLabWork();
            iterator =labWorks.iterator();
            while (iterator.hasNext()) {
                LabWork labWork = (LabWork) iterator.next();
                if (labWork.compareTo(l) > 0) {
                    iterator.remove();
                    labWorks.remove(labWork);
                }
            }
            System.out.println("Команда успешно выполнена.");
        } else System.out.println("В коллекции отсутствуют элементы. Выполнение команды не возможно.");
    }

    /**
     *  Удаляет первый элемент из коллекции
     */
    public void remove_first() {
        if (labWorks.size() != 0) {
            iterator = labWorks.iterator();
            LabWork l = (LabWork) iterator.next();
            iterator.remove();;
            labWorks.remove(l);
            System.out.println("Команда успешно выполнена.");
        } else System.out.println("В коллекции отсутствуют элементы. Выполнение команды не возможно.");
    }

    /**
     * Выводит среднее значение поля averagePoint для всех элементов коллекции
     */
    public void average_of_average_point() {
        if (labWorks.size() != 0) {
            float sum_average_point = 0;
            iterator = labWorks.iterator();
            while (iterator.hasNext()) {
                sum_average_point += ((LabWork)iterator.next()).getAveragePoint();
            }
            System.out.println("Cреднее значение поля averagePoint для всех элементов коллекции: " + sum_average_point/labWorks.size());
        }
        else System.out.println("В коллекции отсутствуют элементы. Выполнение команды не возможно.");
    }

    /**
     * Выводит значения поля averagePoint всех элементов в порядке убывания
     */
    public void print_field_descending_average_point(){
        if (labWorks.size() != 0) {
            TreeSet<Double> averagePoint = new TreeSet<>();
            iterator = labWorks.iterator();
            while (iterator.hasNext()) {
                averagePoint.add(((LabWork)iterator.next()).getAveragePoint());
            }
            int a = averagePoint.size();
            for (int i = 0; i < a; i++) System.out.println(averagePoint.pollLast());
        } else System.out.println("В коллекции отсутствуют элементы. Выполнение команды не возможно.");
    }

    /**
     * Выводит элементы, значение поля averagePoint которых равно заданному
     * @param averagePoint_s : значение поля avergePoint
     */
    public void filter_by_average_point(String averagePoint_s){
        if (labWorks.size() != 0) {
            try {
                boolean b = true;
                Double averagePoint = Double.parseDouble(averagePoint_s);
                iterator = labWorks.iterator();
                while (iterator.hasNext()) {
                    LabWork lab = (LabWork) iterator.next();
                    if (lab.getAveragePoint().equals(averagePoint)) {
                        System.out.println(lab.toString());
                        b = false;
                    }
                }
                if (b) System.out.println("Элементов с значением поля averagePoint = " + averagePoint_s + " не найдено");
            } catch (NumberFormatException ex) {
                System.out.println("Аргумент не является значением типа Integer");
            }
        } else System.out.println("В коллекции отсутствуют элементы. Выполнение команды не возможно.");
    }

    /**
     *  Десериализует коллекцию из файла json.
     */
    public void load() {
        int beginSize = labWorks.size();
        try {
            if (!xmlCollection.exists()) throw new FileNotFoundException();
        } catch (FileNotFoundException ex) {
            System.out.println("Файла по указанному пути не существует.");
            if (!wasStart) System.exit(1);
            else return;
        }
        try {
            if (!xmlCollection.canRead() || !xmlCollection.canWrite()) throw new SecurityException();
        } catch (SecurityException ex) {
            System.out.println("Файл защищён от чтения и/или записи. Для работы программы нужны оба разрешения.");
            if (!wasStart) System.exit(1);
            else return;
        }
        try {
            if (xmlCollection.length() == 0) throw new XMLParseException("");
        } catch (XMLParseException ex) {
            System.out.println("Файл пуст.");
            if (!wasStart) System.exit(1);
            else return;
        }
        try {
            System.out.println("Loading a collection " + xmlCollection.getAbsolutePath());
            // мы можем создать экземпляр JDOM Document из классов DOM, SAX и STAX Builder
            org.jdom2.Document jdomDocument = createJDOMusingSAXParser(collectionPath);
            Element root = jdomDocument.getRootElement();
            // получаем список всех элементов
            List<Element> labWorkListElements = root.getChildren("LabWork");
            // список объектов Student, в которых будем хранить
            // считанные данные по каждому элементу
            for (Element lab : labWorkListElements) {
                Integer id = Integer.parseInt(lab.getChildText("id"));
                String name = lab.getChildText("name");
                List<Element> lab_c = lab.getChildren("Coordinates");
                int x = Integer.parseInt(lab_c.get(0).getChildText("x"));
                Double y = Double.parseDouble(lab_c.get(0).getChildText("y"));
                LocalDate creationDate = LocalDate.parse(lab.getChildText("creationDate"));
                long minimalPoint = Long.parseLong(lab.getChildText("minimalPoint"));
                String description = lab.getChildText("description");
                Double averagePoint = Double.parseDouble(lab.getChildText("averagePoint"));
                Difficulty difficulty = null;
                String difficulty_s = lab.getChildText("difficulty");
                if (!difficulty_s.equals("null")) difficulty = Difficulty.valueOf(difficulty_s);
                List<Element> lab_d = lab.getChildren("Discipline");
                String nameDiscipline = lab_d.get(0).getChildText("name");
                Long labsCount = null;
                String labsCount_s = lab_d.get(0).getChildText("labsCount");
                if (!labsCount_s.equals("null")) labsCount = Long.parseLong(labsCount_s);
                labWorks.add(new LabWork(id, name, new Coordinates(x, y), creationDate, minimalPoint, description, averagePoint, difficulty, new Discipline(nameDiscipline, labsCount)));
            }
        }catch (Exception e) {
            System.out.println("Не удалось загрузить коллекцию. Всё очеь-очень плохо!");
            if (!wasStart) System.exit(1);
            else return;
        }
        System.out.println("Коллекция успешно загружена. Добавлено " + (labWorks.size() - beginSize) + " элементов.");
    }

    private static org.jdom2.Document createJDOMusingSAXParser(String fileName)
            throws JDOMException, IOException {
        SAXBuilder saxBuilder = new SAXBuilder();
        return saxBuilder.build(new File(fileName));
    }

    /**
     * Выводит информацию о коллекции.
     */
    @Override
    public String toString() {
        return "Тип коллекции: " + labWorks.getClass() +
                "\nДата инициализации: " + initDate +
                "\nКоличество элементов: " + labWorks.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CollectionManager)) return false;
        CollectionManager manager = (CollectionManager) o;
        return labWorks.equals(manager.labWorks) &&
                xmlCollection.equals(manager.xmlCollection) &&
                initDate.equals(manager.initDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(labWorks, initDate);
    }
}
