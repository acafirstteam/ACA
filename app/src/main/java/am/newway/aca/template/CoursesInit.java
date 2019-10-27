package am.newway.aca.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public
class CoursesInit {

    public static
    List<Course> addCourse () {
        List<Course> courses = new ArrayList<>();
        Course course;
        course = new Course( "Introduction to JavaScript" ,
                "https://aca.am/en/intro-js.html" , false , 0 ,
                "https://aca.am/assets/img/intro-level/js.png" );
        course.setDescription( getDescription( 1 ) );
        course.setGroup_name( getGroup( course.getGroup() ) );
        courses.add( course );
        course = new Course( "Introduction to Java" ,
                "https://aca.am/en/intro-java.html" , false , 0 ,
                "https://aca.am/assets/img/intro-level/java.png" );
        course.setDescription( getDescription( 2 ) );
        course.setGroup_name( getGroup( course.getGroup() ) );
        courses.add( course );
        course = new Course( "IOS fundamentals" ,
                "https://aca.am/en/intro-ios.html" , false , 0 ,
                "https://aca.am/assets/img/intro-level/ios.png" );
        course.setDescription( getDescription( 3 ) );
        course.setGroup_name( getGroup( course.getGroup() ) );
        courses.add( course );
        course = new Course( "QA fundamentals" ,
                "https://aca.am/en/qe-fundamentals.html" , false , 0 ,
                "https://aca.am/assets/img/courses/qa-fundamentals.png" );
        course.setDescription( getDescription( 4 ) );
        course.setGroup_name( getGroup( course.getGroup() ) );
        courses.add( course );
        course = new Course( "UI/UX fundamentals" ,
                "https://aca.am/en/intro-ui-ux.html" , false , 0 ,
                "https://aca.am/assets/img/intro-level/ui-ux.png" );
        course.setDescription( getDescription( 5 ) );
        course.setGroup_name( getGroup( course.getGroup() ) );
        courses.add( course );
        course = new Course( "Introduction to C++" ,
                "https://aca.am/en/introduction.html" , false , 0 ,
                "https://aca.am/assets/img/intro-level/cpp.png" );
        course.setDescription( getDescription( 6 ) );
        course.setGroup_name( getGroup( course.getGroup() ) );
        courses.add( course );
        course = new Course( "Introduction to Python" ,
                "https://aca.am/en/intro-python.html" , false , 0 ,
                "https://aca.am/assets/img/intro-level/python.png" );
        course.setDescription( getDescription( 7 ) );
        course.setGroup_name( getGroup( course.getGroup() ) );
        courses.add( course );
        course = new Course( "Introduction to IT Project Management" ,
                "https://aca.am/en/intro-it-pm.html" , false , 0 ,
                "https://aca.am/assets/img/intro-level/itpm-logo.png" );
        course.setDescription( getDescription( 8 ) );
        course.setGroup_name( getGroup( course.getGroup() ) );
        courses.add( course );
        course =
                new Course( "NodeJS" , "https://aca.am/en/nodejs.html" , false , 1 ,
                        "https://aca.am/assets/img/nodejs/logo-nodejs.png" );
        course.setDescription( getDescription( 9 ) );
        course.setGroup_name( getGroup( course.getGroup() ) );
        courses.add( course );
        course = new Course( "UI/UX" , "https://aca.am/en/ui-ux.html" , false , 1 ,
                "https://aca.am/assets/img/ui-ux/ui_ux_logo.png" );
        course.setDescription( getDescription( 10 ) );
        course.setGroup_name( getGroup( course.getGroup() ) );
        courses.add( course );
        course =
                new Course( "JavaScript" , "https://aca.am/en/js.html" , false , 1 ,
                        "https://aca.am/assets/img/courses/js.png" );
        course.setDescription( getDescription( 11 ) );
        course.setGroup_name( getGroup( course.getGroup() ) );
        courses.add( course );
        course = new Course( "JAVA" , "https://aca.am/en/java.html" , false , 1 ,
                "https://aca.am/assets/img/courses/java.jpg" );
        course.setDescription( getDescription( 12 ) );
        course.setGroup_name( getGroup( course.getGroup() ) );
        courses.add( course );
        course = new Course( "Android" , "https://aca.am/en/android.html" , false ,
                1 ,
                "https://aca.am/assets/img/courses/android.png" );
        course.setDescription( getDescription( 13 ) );
        course.setGroup_name( getGroup( course.getGroup() ) );
        courses.add( course );
        course = new Course( "iOS" , "https://aca.am/en/ios.html" , false , 1 ,
                "https://aca.am/assets/img/courses/ios.png" );
        course.setDescription( getDescription( 14 ) );
        course.setGroup_name( getGroup( course.getGroup() ) );
        courses.add( course );
        course = new Course( "QA automation" , "https://aca.am/en/qe.html" , false ,
                1 ,
                "https://aca.am/assets/img/courses/qa.png" );
        course.setDescription( getDescription( 15 ) );
        course.setGroup_name( getGroup( course.getGroup() ) );
        courses.add( course );
        course = new Course( "C#" , "https://aca.am/en/csharp.html" , false , 1 ,
                "https://aca.am/assets/img/courses/csharp.png" );
        course.setDescription( getDescription( 16 ) );
        course.setGroup_name( getGroup( course.getGroup() ) );
        courses.add( course );
        course =
                new Course( "Python" , "https://aca.am/en/python.html" , false , 1 ,
                        "https://aca.am/assets/img/courses/Python.png" );
        course.setDescription( getDescription( 17 ) );
        course.setGroup_name( getGroup( course.getGroup() ) );
        courses.add( course );
        course = new Course( "C++" , "https://aca.am/en/cpp.html" , false , 1 ,
                "https://aca.am/assets/img/courses/c++.png" );
        course.setDescription( getDescription( 18 ) );
        course.setGroup_name( getGroup( course.getGroup() ) );
        courses.add( course );
        course = new Course( "Tech for Entrepreneurs and Managers" ,
                "https://aca.am/en/computer-science.html" , true , 1 ,
                "https://aca.am/assets/img/computerscience/logo.png" );
        course.setDescription( getDescription( 19 ) );
        course.setGroup_name( getGroup( course.getGroup() ) );
        courses.add( course );
        course = new Course( "IT project management" , "null" , true , 1 ,
                  "" );
        course.setDescription( getDescription( 20 ) );
        course.setGroup_name( getGroup( course.getGroup() ) );
        courses.add( course );
        course = new Course( "Algorithms" , "https://aca.am/en/algorithms.html" ,
                false , 2 ,
                "https://aca.am/assets/img/courses/alg_transparent.png" );
        course.setDescription( getDescription( 21 ) );
        course.setGroup_name( getGroup( course.getGroup() ) );
        courses.add( course );
        course = new Course( "React.js" , "https://aca.am/en/react.html" , false ,
                2 ,
                "https://aca.am/assets/img/react/logo.png" );
        course.setDescription( getDescription( 22 ) );
        course.setGroup_name( getGroup( course.getGroup() ) );
        courses.add( course );
        course = new Course( "UX" , "null" , true , 2 ,
                "" );
        course.setDescription( getDescription( 23 ) );
        course.setGroup_name( getGroup( course.getGroup() ) );
        courses.add( course );
        course = new Course( "Big Data" , "null" , true , 2 ,
                 "" );
        course.setDescription( getDescription( 24 ) );
        course.setGroup_name( getGroup( course.getGroup() ) );
        courses.add( course );

        return courses;
    }

    private static
    Map<String, Object> getGroup ( int n ) {
        Map<String, Object> map = new HashMap<>();

        switch ( n ) {
            case 0:
                map.put( "en" ,
                        "COURSES FOR BEGINNERS" );
                map.put( "hy" ,
                        "ԴԱՍԸՆԹԱՑՆԵՐ ՍԿՍՆԱԿՆԵՐԻ ՀԱՄԱՐ" );
                break;
            case 1:
                map.put( "en" ,
                        "SPECIALIZED COURSES" );
                map.put( "hy" ,
                        "ՄԱՍՆԱԳԻՏԱՑՎԱԾ ԴԱՍԸՆԹԱՑՆԵՐ" );
                break;
            case 2:
                map.put( "en" ,
                        "ADVANCED COURSES" );
                map.put( "hy" ,
                        "ԽՈՐԱՑՎԱԾ ԴԱՍԸՆԹԱՑՆԵՐ" );
                break;
        }
        return map;
    }

    private static
    Map<String, Object> getDescription ( int n ) {
        Map<String, Object> map = new HashMap<>();

        switch ( n ) {

            case 1:
                map.put( "en" ,
                        "This introductory course is perfect for those who want to master web development and are making their very first steps. The students will be provided will allow you to lay a firm foundation for basic programming concepts and simultaneously advance knowledge in JavaScript." );
                map.put( "hy" ,
                        "Այս դասընթացը նախատեսված է նրանց համար, ովքեր հետաքրքրված են JavaScript ծրագրավորման լեզվով և ցանկանում են անել իրենց առաջին քայլերը ծրագրավորման ոլորտում։ Վեց շաբաթվա ընթացքում պատկերացում կկազմեք ծրագրավորման հիմքային կոնցեպտների մասին՝ օգտագործելով JavaScript լեզուն, ինչպես նաև կուսումնասիրենք HTML/CSS։" );
                break;

            case 2:
                map.put( "en" ,
                        "This introductory course is perfect for those who want to master Java programming and are making their very first steps. On the duration of the course you will lay a firm foundation for basic programming concepts and simultaneously advance knowledge in Java." );
                map.put( "hy" ,
                        "Introduction to Java դասընթացը հրաշալի հնարավորություն է բոլոր նրանց համար, ովքեր ցանկանում են անել առաջին քայլերը ծրագրավորման ոլորտում։ Դասընթացի ընթացքում ուսանողները նախնական գիտելիքներ կստանան ծրագրավորման հիմունքներից Java ծրագրավորման լեզվով։" );
                break;
            case 3:
                map.put( "en" ,
                        "This introductory course is perfect for those who want to master iOS development and are making their very first steps. On the duration of the course you will lay a firm foundation for basic programming concepts and simultaneously advance knowledge in Swift." );
                map.put( "hy" ,
                        "Introduction to iOS դասընթացը հրաշալի հնարավորություն է բոլոր նրանց համար, ովքեր ցանկանում են անել առաջին քայլերը iOS ծրագրավորման ոլորտում։ Դասընթացի ընթացքում ուսանողները նախնական գիտելիքներ կստանան ծրագրավորման հիմունքներից Swift ծրագրավորման լեզվով։" );
                break;
            case 4:
                map.put( "en" ,
                        "This course is designed for those who wish to become quality assurance engineers.No prior knowledge of programming is required. During the course, you'll learn basic concepts of testing. As a quality assurance engineer you will explore the principles of working with Agile and Waterfall, will have a complete insight into the stages and types of testing, and will apply that knowledge to software testing. In the final part of the course, you will explore web-based automated testing using Javascript and Selenium. Course program has been based on requirements of leading IT companies, which will guarantee your future employment." );
                map.put( "hy" , "Այս դասընթացի նպատակն է պատրաստել որակի ապահովման " +
                        "ինժիներներ։ Մասնակցելու համար անհրաժեշտ չեն ծրագրավորման նախնական " +
                        "գիտելիքներ։" +
                        "Դասընթացի ընթացքում կսովորեք թեստավորման հիմնական գաղափարները , " +
                        "կուսումնասիրեք" +
                        "Agile and Waterfall մեթոդներով աշխատելու սկզբունքները որպես որակի ապահովման" +
                        "ինժեներներ , ամբողջական պատկերացում կկազմեք թեստավորման փուլերի և տեսակների" +
                        "մասին և գործնականորեն կկիրառեք այդ գիտելիքները իրական ծրագրերի ստուգման " +
                        "համար ," +
                        "ինչպես նաև ուսումնասիրելու եք վեբ ծրագրերի ավտոմատացված ստուգումը " +
                        "օգտագործելով" + "ժամանակակից լեզուներ և գրադարաններ։" +
                        "Դասընթացը հաջողությամբ ավարտելուց հետո դուք կստանաք վկայական և աշխատանքի" +
                        "հնարավորություն հայաստանյան առաջատար IT կազմակերպություններում։" );
                break;
            case 5:
                map.put( "en" ,
                        "This introductory course is perfect for those making their very first steps in web and mobile design. The course is outlined to familiarize the students with basic principles and fundamentals in visual art and design. The students will be introduced to terminology necessary to communicate concepts and theory in art and design and teach them to create computer-based projects using Adobe Photoshop and Illustrator software programs." );
                map.put( "hy" ,
                        "Այս դասընթացը նախատեսված է նրանց համար, ովքեր ցանկանում են մասնագիտանալ UI/UX դիզայնի մեջ։" );
                break;
            case 6:
                map.put( "en" ,
                        "This introductory course is perfect for those who want to master C++ programming and are making their very first steps. On the duration of the course you will lay a firm foundation for basic programming concepts and simultaneously advance knowledge in C++." );
                map.put( "hy" ,
                        "Այս դասընթացը նախատեսված է նրանց համար, ովքեր ուզում են անել իրենց առաջին քայլերը ծրագրավորման ոլորտում։ Դասընթացի ընթացքում մենք կլուծենք բազմաթիվ խնդիրներ, կսովորենք որոշ ալգորիթմներ, և կհմտանանք ներկայիս ամենատարածված ծրագրավորման լեզուներից մեկում ՝ C++ում։" );
                break;
            case 7:
                map.put( "en" ,
                        "This introductory course is perfect for those who want to master Python development and are making their very first steps. The students will be provided will allow you to lay a firm foundation for basic programming concepts and simultaneously advance knowledge in Python." );
                map.put( "hy" ,
                        "«Python» դասընթացը հրաշալի հնարավորություն է բոլոր նրանց համար, ովքեր ցանկանում են անել առաջին քայլերը Python ծրագրավորման ոլորտում։ Դասընթացի ընթացքում ուսանողները նախնական գիտելիքներ կստանան ծրագրավորման հիմունքներից Python ծրագրավորման լեզվով։" );
                break;
            case 8:
                map.put( "en" ,
                        "This introductory course by ACA is perfect for those who want to take their very first steps in IT Project Management. It will lay a firm foundation for basic concepts and different methodologies of software project management used in the business space. With the help of our experienced instructors, you will learn about the different types of software projects and the challenges that each of these types poses in front of a project manager. Enroll now and get one of the most demanded professions of today!" );
                map.put( "hy" ,
                        "ACA-ի այս ներածական դասընթացը կատարյալ է նրանց համար, ովքեր ցանկանում են իրենց առաջին քայլերը կատարել  IT Project Management-ի մեջ: Այն ամուր հիմք կծառայի բիզնես ոլորտում օգտագործվող  կառավարման հիմնական հասկացությունների և կիրառվող բազմազան մեթոդների համար:  Մեր փորձառու դասընթացավարի օգնությամբ դուք կծանոթանք ծրագրերի տարբեր տեսակների և մարտահրավերների հետ, որոնց բախվում է յուրաքանչյուր ծրագրերի կառավարման մասնագետ: Գրանվի՛ր հիմա և ստացիր ներկայումս ամենամեծ պահանջարկ վայելող մասնագիտություններից մեկը: " );
                break;
            case 9:
                map.put( "en" ,
                        "This course covers the fundamentals of Node.js before diving deep into algorithms, practical problems and the great tools that will help you solve them. The goal is turn you into a professional Node.js developer capable of developing, testing, and deploying real-world production applications. After the succesful completion of the course best students will get recruited by our partner companies." );
                map.put( "hy" ,
                        "Այս դասընթացը մշակված է նրանց համար, ովքեր ցանկանում են սովորել ծրագրավորման արդի տեխնոլոգիաներից մեկը՝ Node.js-ը, որն ուղեկցվում է JavaScript լեզվի ուսումնասիրությամբ։ Դասընթացի ընթացքում մենք կլուծենք բազմաթիվ խնդիրներ, կսովորենք որոշ ալգորիթմներ, և կհմտանանք ներկայիս ամենատարածված ծրագրավորման լեզուներից մեկում՝Javascript-ում։ Ծրագիրը միավորում է տեսական և գործնական գիտելիքներ՝ հնարավորություն ընձեռելով ձեր գաղափարների հիման վրա բազմաֆունկցիոնալ կայքեր մշակել։ Հաջողությամբ ավարտելուց հետո դուք կստանաք վկայական և աշխատանքի հնարավորություն հայաստանյան առաջատար ՏՏ ընկերություններում։" );
                break;
            case 10:
                map.put( "en" ,
                        "This course is perfect for those who want to specialize in UI/UX design. During the classes you will learn creating user-centered and smart designs for web and mobile products. Figma, Adobe XD and inVision are the design tools you will be using throughout the course. You will have the hands-on experience of working on real projects as well as be taught to present your work in your portfolio in the best possible way." );
                map.put( "hy" ,
                        "Այս դասընթացը նախատեսված է նրանց համար, ովքեր ցանկանում են մասնագիտանալ UI/UX դիզայնի մեջ։ Դասընթացների ժամանակ կսովորեք մշակել վեբ կայքերի և հավելվածների այնպիսի դիզայններ, որոնք հարմարավետ և ինտուիտիվ կլինեն օգտատերերի համար։ Աշխատանքները կկատարվեն Figma դիզայներական գործիքով, Adobe XD և inVision-ով։ Դասընթացների ընթացքում կիրականացնեք իրական նախագծեր, ինչպես նաև կսովորեք թե ինչպես ճիշտ մատուցել Ձեր կատարած աշխատանքները Ձեր պորտֆոլիոյում։" );
                break;
            case 11:
                map.put( "en" ,
                        "This course is designed for those, who want to make first steps in programming. During this course we will solve various problems, learn some algorithms and will become skilled in one of the most widespread programming languages nowadays - JavaScript ." );
                map.put( "hy" ,
                        "Այս դասընթացը մշակված է նրանց համար, ովքեր ցանկանում են սովորել ծրագրավորման արդի տեխնոլոգիաներից մեկը՝ React-ը, որն ուղեկցվում է JavaScript լեզվի ուսումնասիրությամբ։ Դասընթացի ընթացքում մենք կլուծենք բազմաթիվ խնդիրներ, կսովորենք որոշ ալգորիթմներ, և կհմտանանք ներկայիս ամենատարածված ծրագրավորման լեզուներից մեկում՝Javascript-ում։ Ծրագիրը միավորում է տեսական և գործնական գիտելիքներ՝ հնարավորություն ընձեռելով ձեր գաղափարների հիման վրա բազմաֆունկցիոնալ կայքեր մշակել։ Հաջողությամբ ավարտելուց հետո դուք կստանաք վկայական և աշխատանքի հնարավորություն հայաստանյան առաջատար ՏՏ ընկերություններում։" );
                break;
            case 12:
                map.put( "en" ,
                        "This course is designed for those who wish to learn JAVA , and gain experience of development using spring. Course program has been based on requirements of leading IT companies, which will guarantee your future employment. Classes will be held 3 times per week from 18-30 to 20-30 on Wednesdays and Fridays, and from 16 to 19 on Sundays." );
                map.put( "hy" ,
                        "Այս դասընթացը նախատեսված է նրանց համար, ովքեր ուզում են սովորել JAVA, և իրականացնել նախագծեր օգտագործելով Spring. Ծրագիրը միավորում է տեսական և գործնական գիտելիքներ՝ հնարավորություն ընձեռելով ձեր գաղափարների հիման վրա բազմաֆունկցիոնալ ծրագրեր մշակել։ Դասընթացը հաջողությամբ ավարտելուց հետո դուք կստանաք վկայական և աշխատանքի հնարավորություն հայաստանյան առաջատար IT կազմակերպություններում։" );
                break;
            case 13:
                map.put( "en" ,
                        "This course is designed for those who wish to learn Android application development. Our program combines theory and practice to help you build great apps from scratch. Course program has been based on requirements of leading IT companies, which will guarantee your future employment." );
                map.put( "hy" ,
                        "Այս դասընթացը նախատեսված է նրանց համար, ովքեր ուզում են սովորել ստեղծել Android հավելվածներ։ Ծրագիրը միավորում է տեսական և գործնական գիտելիքներ, հնարավորություն ընձեռելով ձեր գաղափարների հիման վրա բազմաֆունկցիոնալ ծրագրեր մշակել։ Դասընթացը հաջողությամբ ավարտելուց հետո դուք կստանաք վկայական, և աշխատանքի հնարավորություն հայաստանյան առաջատար IT կազմակերպություններում։" );
                break;
            case 14:
                map.put( "en" ,
                        "This course is designed for those who wish to learn iOS application development using Objective C and Swift. Main objectives are not only iOS development, but also skills, tools and qualities for becoming a high quality developer. Our program combines theory and practice to help you build great apps from scratch. Course program has been based on requirements of leading IT companies, which will guarantee your future employment." );
                map.put( "hy" ,
                        "Այս դասընթացը նախատեսված է նրանց համար, ովքեր ուզում են սովորել ստեղծել iOS հավելվածներ։ Ծրագիրը միավորում է տեսական և գործնական գիտելիքներ՝ հնարավորություն ընձեռելով ձեր գաղափարների հիման վրա բազմաֆունկցիոնալ ծրագրեր մշակել։ Դասընթացը հաջողությամբ ավարտելուց հետո դուք կստանաք վկայական և աշխատանքի հնարավորություն հայաստանյան առաջատար IT կազմակերպություններում։" );
                break;
            case 15:
                map.put( "en" ,
                        "This course is aimed at training Quality Assurance Automation Engineers. During the course we will explore programming basics in QA as well as programming basics using Python. We will also learn testing using web automation frameworks such as Selenium WebDriver and Pytest." );
                map.put( "hy" ,
                        "Այս դասընթացի նպատակն է պատրաստել ավտոմատացում իրականացնող որակի ապահովման ինժեներներ։ Դասընթացի ընթացքում անդրադարձ է կատարվելու դեպի ծրագրային որակի ապահովման հիմունքներին, ինչպես նաև ծրագրավորման հիմունքներին օգտագործելով Python ծրագրավորման լեզուն։ Դասընթացի ընթացքում մասնակիցները կսովորեն կատարել ծրագրային միջոցների ավտոմավացված ստուգում օգտագործելով Selenium WebDriver և Pytest գործիքները։" );
                break;
            case 16:
                map.put( "en" ,
                        "This course is designed for those who wish to learn Microsoft.NET technology and C# language. Course program has been based on requirements of leading IT companies, which will guarantee your future employment." );
                map.put( "hy" ,
                        "Այս դասընթացը մշակված է նրանց համար, ովքեր ցանկանում են սովորել ծրագրավորման արդի տեխնոլոգիաներից մեկը՝ Microsoft.NET-ը, որն ուղեկցվում է C# օբյեկտային լեզվի ուսումնասիրությամբ։ Դասընթացը հաջողությամբ ավարտելուց հետո դուք կստանաք վկայական և աշխատանքի հնարավորություն հայաստանյան առաջատար ՏՏ ընկերություններում։" );
                break;
            case 17:
                map.put( "en" ,
                        "This course is designed for those who wish to learn one of the most cutting edge programming languages- Python, and gain experience of developing real projects using Python. Classes will be held 3 times per week on from 18-30 to 20-30 on Tuesdays and Thursdays, and from 11 to 15 on Sundays." );
                map.put( "hy" ,
                        "Այս դասընթացը մշակված է այն մարդկանց համար, ովքեր ցանկանում են սովորել ամենաժամանակից և արագ զարգացող լեզուներից մեկը` Python-ը, և ձեռք բերել փորձ՝ աշխատելով իրական նախագծերի վրա՝ օգտագործելով Python ծրագրավորման լեզուն։ Հանդիպում ենք շաբաթը երեք անգամ Երեքշաբթի և Հինգշաբթի օրերին 18։30-20:30, իսկ Շաբաթ օրը 11:00-15:00-ն։" );
                break;
            case 18:
                map.put( "en" ,
                        "Our new “Intermediate C++” course is designed for people with a basic but firm programming background who have decided to pursue a career in C++ development. The course covers an extremely wide range of topics relating to C++, Data structures, Object-Oriented programming and design, basics of GUI programming, multithreading, and more." );
                map.put( "hy" ,
                        "Մեր նոր` «Intermediate C++» դասընթացը նախատեսված է այն մարդկանց համար, ովքեր ունեն ծրագրավորման նախնական գիտելիքներ, և որոշել են իրենց կարիերան շարունակել C ++ ծրագրավորման լեզվով: Դասընթացն ընդգրկում է թեմաների  լայն շրջանակ ՝ C ++, Data structures, Object-Oriented programming and design, basics of GUI programming, multithreading, and և այլն:" );
                break;
            case 19:
                map.put( "en" ,
                        "This course is for managers and entrepreneurs who want to UNDERSTAND tech. If you have a team, a project, or a company, for which some of the product is related to tech then it is a great advantage for you to understand the tech involved because it will help you better manage the team.How to communicate with your programmers better about the technical parts of the project? Do the programmers that you are managing or hiring use the best technologies to build the necessary app? Is the programmer in your team, who keeps saying he is working on a difficult problem working efficiently or is he being lazy?How easy is it to build the feature you want? If you have a team, a project, or a company, for which some of the product is related to tech then it is a great advantage for you to understand the tech involved because it will help you better manage the team. How to communicate with your programmers better about the technical parts of the project? Do the programmers that you are managing or hiring use the best technologies to build the necessary app? Is the programmer in your team, who keeps saying he is working on a difficult problem working efficiently or is he being lazy?How easy is it to build the feature you want? If answering any of these questions would make your project more successful then this is the course for you! And these all are questions you can answer after completing our course!" );
                map.put( "hy" , "Այս դասընթացը նախատեսված է այն մենեջերների և" +
                        "ձեռնարկատերերի համար , ովքեր ցանկանում են «հասկանալ» տեխնոլոգիաները:Եթե" +
                        "ունեք թիմ , նախագիծ կամ ընկերություն, որտեղ արտադրանքի մի մասը կապված է" +
                        "տեխնոլոգիաների հետ , ապա ձեզ համար մեծ առավելություն կլինի հասկանալ և" +
                        "ներգրավված լինել այս դաշտում , քանի որ այն Ձեզ կօգնի ավելի լավ ղեկավարել " +
                        "թիմը: Ինչպե՞ս ավելի լավ հաղորդակցվել Ձեր ծրագրավորողների հետ ծրագրի տեխնիկական" +
                        "մասերի վերաբերյալ:" +
                        "Արդյո՞ք ծրագրավորողները, ում դուք ղեկավարում եք կամ վարձում եք, օգտագործում են" +
                        "լավագույն տեխնոլոգիաները `անհրաժեշտ ծրագիրը ստեղծելու համար: Ծրագրավորողն, " +
                        "ով շարունակում է ասել, որ աշխատում է բարդ խնդրի վրա, արդյոք " +
                        "արդյունավետ աշխատում է, թե ծույլ է, որքանո՞վ է հեշտ ստեղծել ձեր  ցանկացածը:" +
                        "Եթե այս հարցերից որևէ մեկին պատասխանելը ձեր նախագիծն ավելի հաջողակ կդարձնի, ապա վերջինս " +
                        "հենց Ձե՛զ համար է: Եվ այս բոլոր հարցերի պատասխանները կունենաք դասընթացն ավարտելուց անմիջապես հետո:" );
                break;
            case 20:
                map.put( "en" , "" );
                map.put( "hy" ,
                        "" );
                break;
            case 21:
                map.put( "en" ,
                        "This course is designed for those who want to learn design and analysis of efficient algorithms, simultaneously implementing them in practice. This course is a must learn not only for junior developers, but also for developers who want to sharpen their skills and get deep understanding of programming concepts." );
                map.put( "hy" ,
                        "Այս դասընթացը նախատեսված է նրանց համար, ովքեր ցանկանում են սովորել " +
                                "նախագծել և ուսումնասիրել ալգորիթմներ` միաժամանակ" +
                                "կիրառելով դրանք պրակտիկ նախագծերում։Այս կուրսը չափազանց օգտակար կլինի " +
                                "ինչպես" +
                                "սկսնակ ծրագրավարողներին , այնպես էլ ` արդեն փորձառու ծրագրավորողներին," +
                                "որոնց կօգնի բարձրացնել իրենց աշխատանքի էֆֆեկտիվությունը։" );
                break;
            case 22:
                map.put( "en" ,
                        "We are happy to announce the launch of an intensive ReactJS course. If you are skilled in Vanilla JS or if you have experience with other Javascript frameworks, but would like to dive deeper into React than we have great news for you. Within the frame of this course we will study the ReactJS library from A to Z and also technologies like WebPack and Babel. Starting from the basic React elements we will proceed to advanced topics like Universal apps, SSR, static React apps, Bundle size optimization, i18n and animations. We'll not just scratch the surface but dive deeply into React as well as popular libraries like Redux and its alternatives. The course is aimed to teach the very intricacies of this library through which you can quickly build amazing and powerful web apps.We value practical knowledge as much as theoretical. That is why while studying the typical project ecosystem & tooling, SPAs, routing (with react-router v4), deployment with Nginx, interaction with APIs you will have the opportunity to use the latter knowledge in implementing your group project during the course." );
                map.put( "hy" ,
                        "Ուրախ ենք ազդարարել ReactJS ինտենսիվ դասընթացի մեկնարկը:Եթե ​​" +
                                "դուք հմուտ եք Vanilla JS - ում կամ ունեք Javascript - ի այլ framework - ների​​" +
                                "հետ աշխատելու փորձ , և կցանկանաք ավելի խորն ուսումնասիրել React - ը , ապա մենք " +
                                "ունենք​​" + "հիանալի նորություն ձեզ համար.​​" +
                                "Այս դասընթացի շրջանակներում մենք կուսումնասիրենք ReactJS library - ն​​" +
                                "ամբողջությամբ , ինչպես նաև կուսումնասիրենք WebPack - ի և Babel -ի նման " +
                                "տեխնոլոգիաները:​​" +
                                "Սկսելով React -ի հիմնական տարրերից ՝մենք անցնելու ենք մասնագիտացված թեմաների ՝​​" +
                                "Universal ծրագրեր, SSR, ստատիկ React ծրագրեր, փաթեթների չափի օպտիմիզացիա , i18n​​" +
                                "և անիմացիաներ:Մենք ոչ միայն մակերեսոն կծանոթանանք, այլ նաև խորը կուսումնասիրենք " +
                                "React​​" +
                                "-ը , ինչպես նաև հանրահայտ library -ներից Redux - ը և վերջինիս ալտերնատիվ " +
                                "տարբերակները:​​" +
                                "Դասընթացը նպատակ ունի ուսուցանել այս library - ի բարդությունը , որի միջոցով հնարավոր" +
                                " է​​" + "արագ ստեղծել հրաշալի և հզոր վեբ հավելվածներ:​​" +
                                "Մենք գործնական գիտելիքները կարևորում ենք այնքան , որքան և տեսականը:Ահա թե ինչու " +
                                "Typical​​" +
                                "project ecosystem & tooling , SPAs , routing( with react - router v4 ) , և " +
                                "deployment​​" +
                                "with Nginx ուսումնասիրելիս , և API - ների հետ գործ ունենալուց, դուք​​" +
                                "հնարավորություն կունենաք օգտագործել վերջին գիտելիքները դասընթացի ընթացքում ձեր խմբի​​" +
                                "նախագիծը կյանքի կոչելու համար " );
                break;
            case 23:
                map.put( "en" , "" );
                map.put( "hy" ,
                        "" );
                break;
            case 24:
                map.put( "en" , "" );
                map.put( "hy" ,
                        "" );
                break;

        }
        return map;
    }
}