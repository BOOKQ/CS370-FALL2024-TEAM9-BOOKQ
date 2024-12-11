create database bookq;
use bookq;

CREATE TABLE accounts (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    firstname VARCHAR(50) NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE books (
    BookID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    Bookname VARCHAR(255) NOT NULL,
    Author VARCHAR(255) NOT NULL,
    ISBN_ID VARCHAR(13) NOT NULL UNIQUE,
    Genre VARCHAR(100) DEFAULT NULL,
    PageLength INT DEFAULT NULL
);

CREATE TABLE recommendations (
    book_title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    genre VARCHAR(255) NOT NULL,
    ISBN VARCHAR(20) NOT NULL PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS ratings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    book_id INT NOT NULL DEFAULT 0,
    rating TINYINT(1) NOT NULL DEFAULT 0, -- 1 for thumbs up, 0 for thumbs down
    FOREIGN KEY (book_id) REFERENCES books(BookID)
);

INSERT INTO books (Bookname, Author, ISBN_ID, Genre, PageLength) VALUES
('To Kill a Mockingbird', 'Harper Lee', '9780061120084', 'Fiction', 281),
('1984', 'George Orwell', '9780451524935', 'Dystopian', 328),
('Pride and Prejudice', 'Jane Austen', '9780141439518', 'Romance', 279),
('The Great Gatsby', 'F. Scott Fitzgerald', '9780743273565', 'Fiction', 180),
('Moby Dick', 'Herman Melville', '9781503280786', 'Fiction', 635),
('War and Peace', 'Leo Tolstoy', '9780140447934', 'Historical Fiction', 1225),
('The Catcher in the Rye', 'J.D. Salinger', '9780316769489', 'Fiction', 277),
('The Hobbit', 'J.R.R. Tolkien', '9780261102217', 'Fantasy', 310),
('The Odyssey', 'Homer', '9780140268867', 'Classic', 500),
('The Book Thief', 'Markus Zusak', '9780375842207', 'Historical Fiction', 552),
('Harry Potter and the Sorcerer\'s Stone', 'J.K. Rowling', '9780439708180', 'Fantasy', 309),
('The Hunger Games', 'Suzanne Collins', '9780439023528', 'Dystopian', 374),
('Brave New World', 'Aldous Huxley', '9780060850524', 'Dystopian', 311),
('The Fault in Our Stars', 'John Green', '9780525478812', 'Romance', 313),
('The Girl on the Train', 'Paula Hawkins', '9781594634024', 'Thriller', 395),
('The Da Vinci Code', 'Dan Brown', '9780307474278', 'Mystery', 489),
('The Chronicles of Narnia', 'C.S. Lewis', '9780066238500', 'Fantasy', 767),
('The Shining', 'Stephen King', '9780307743657', 'Horror', 447),
('The Lord of the Rings: The Fellowship of the Ring', 'J.R.R. Tolkien', '9780547928210', 'Fantasy', 423),
('The Alchemist', 'Paulo Coelho', '9780061122415', 'Adventure', 208),
('The Picture of Dorian Gray', 'Oscar Wilde', '9780141439570', 'Classic', 254),
('Frankenstein', 'Mary Shelley', '9780486282114', 'Gothic Fiction', 280),
('Dracula', 'Bram Stoker', '9780141439846', 'Horror', 416),
('The Silent Patient', 'Alex Michaelides', '9781250301697', 'Psychological Thriller', 325),
('Gone with the Wind', 'Margaret Mitchell', '9781451635621', 'Historical Fiction', 1037),
('The Night Circus', 'Erin Morgenstern', '9780385534635', 'Fantasy', 387),
('The Outsiders', 'S.E. Hinton', '9780142407332', 'Young Adult', 192),
('Fahrenheit 451', 'Ray Bradbury', '9781451673319', 'Dystopian', 158),
('The Handmaid\'s Tale', 'Margaret Atwood', '9780385490818', 'Dystopian', 311),
('The Road', 'Cormac McCarthy', '9780307387899', 'Post-apocalyptic', 287),
('A Game of Thrones', 'George R.R. Martin', '9780553593716', 'Fantasy', 694),
('Catch-22', 'Joseph Heller', '9781451626650', 'Satire', 453),
('The Bell Jar', 'Sylvia Plath', '9780060837020', 'Fiction', 288),
('The Secret Garden', 'Frances Hodgson Burnett', '9780064401883', 'Children\'s Literature', 326),
('The Dark Tower: The Gunslinger', 'Stephen King', '9780452277403', 'Fantasy', 300),
('The Girl with the Dragon Tattoo', 'Stieg Larsson', '9780307454546', 'Thriller', 465),
('The Color Purple', 'Alice Walker', '9780156028356', 'Historical Fiction', 295),
('Sapiens: A Brief History of Humankind', 'Yuval Noah Harari', '9780062316097', 'Non-Fiction', 443),
('Educated', 'Tara Westover', '9780399590504', 'Memoir', 334),
('Becoming', 'Michelle Obama', '9781524763138', 'Memoir', 448),
('The Immortal Life of Henrietta Lacks', 'Rebecca Skloot', '9781400052189', 'Biography', 381),
('Unbroken', 'Laura Hillenbrand', '9780812974492', 'Biography', 498),
('Quiet: The Power of Introverts in a World That Can\'t Stop Talking', 'Susan Cain', '9780307352156', 'Non-Fiction', 368),
('The Power of Habit', 'Charles Duhigg', '9780812981605', 'Self-help', 371),
('The 5 AM Club', 'Robin Sharma', '9781443456623', 'Self-help', 336),
('Atomic Habits', 'James Clear', '9780735211292', 'Self-help', 320),
('Daring Greatly', 'Brené Brown', '9781592408412', 'Self-help', 320),
('Outliers: The Story of Success', 'Malcolm Gladwell', '9780316017930', 'Non-Fiction', 309),
('Thinking, Fast and Slow', 'Daniel Kahneman', '9780374533557', 'Non-Fiction', 499),
('The Tattooist of Auschwitz', 'Heather Morris', '9780062797162', 'Historical Fiction', 288),
('The Silent Corner', 'Dean Koontz', '9780345545972', 'Thriller', 432),
('The Whispering Room', 'Dean Koontz', '9780345545989', 'Thriller', 416),
('Neverwhere', 'Neil Gaiman', '9780380789016', 'Fantasy', 370),
('American Dirt', 'Jeanine Cummins', '9781250209764', 'Fiction', 400),
('Shantaram', 'Gregory David Roberts', '9780312330538', 'Adventure', 936),
('The Night Manager', 'John le Carré', '9780143036351', 'Thriller', 438),
('The Goldfinch', 'Donna Tartt', '9780316055433', 'Fiction', 771),
('All the Light We Cannot See', 'Anthony Doerr', '9781501173219', 'Historical Fiction', 544),
('Circe', 'Madeline Miller', '9780316556347', 'Fantasy', 393),
('A Little Life', 'Hanya Yanagihara', '9780804172707', 'Fiction', 720),
('The Subtle Art of Not Giving a F*ck', 'Mark Manson', '9780062457714', 'Self-help', 224),
('The Handmaid\'s Tale: The Sequel', 'Margaret Atwood', '9780385545614', 'Dystopian', 467),
('Where the Crawdads Sing', 'Delia Owens', '9780735219113', 'Mystery', 368),
('Big Little Lies', 'Liane Moriarty', '9780399164064', 'Fiction', 460),
('The Rosie Project', 'Graeme Simsion', '9781444742979', 'Romance', 368),
('The Nightingale', 'Kristin Hannah', '9780316334416', 'Historical Fiction', 440),
('Room', 'Emma Donoghue', '9780316098322', 'Fiction', 321),
('The Water Dancer', 'Ta-Nehisi Coates', '9780399590603', 'Historical Fiction', 448),
('The Giver of Stars', 'Jojo Moyes', '9780399562488', 'Historical Fiction', 448),
('The Henna Artist', 'Alka Joshi', '9780778308786', 'Fiction', 344),
('The Family Upstairs', 'Lisa Jewell', '9781501197605', 'Thriller', 342),
('The Paris Library', 'Janet Skeslien Charles', '9781982134199', 'Historical Fiction', 400),
('The Thursday Murder Club', 'Richard Osman', '9781984880964', 'Mystery', 380),
('The House in the Cerulean Sea', 'TJ Klune', '9781250217288', 'Fantasy', 400),
('The Song of Achilles', 'Madeline Miller', '9780062060624', 'Historical Fiction', 370),
('Normal People', 'Sally Rooney', '9781984822178', 'Fiction', 273),
('Anxious People', 'Fredrik Backman', '9781501166310', 'Fiction', 368),
('The Invisible Life of Addie LaRue', 'V.E. Schwab', '9780765387561', 'Fantasy', 442);
