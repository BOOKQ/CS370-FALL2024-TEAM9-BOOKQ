<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BOOK LIST</title>
    <link rel="stylesheet" href="https://cdn.datatables.net/1.11.5/css/jquery.dataTables.min.css">
    <style>
            .usu-logo {
                position: absolute;
                bottom: 10px;
                right: 10px;
                width: 100px;
                height: auto;
            }

            body {
                display: flex;
                justify-content: center;
                align-items: center;
                height: 100vh;
                margin: 0;
                background-color: #e0f7fa; /* Light blue background */
                font-family: Arial, sans-serif;
                background-image: url('static/images/1.jpg');
                background-size: cover;
                background-position: center;
                background-repeat: no-repeat;
            }
    </style>
</head>
<body>
    <h1>BOOK LIST</h1>
    <table id="bookTable" class="display">
        <thead>
            <tr>
                <th>Book ID</th>
                <th>Title</th>
                <th>ISBN</th>
                <th>Num Pages</th>
                <th>Publication Date</th>
                <th>Publisher ID</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="book" items="${books}">
            <tr>
                <td>${book.bookId}</td>
                <td>${book.title}</td>
                <td>${book.isbn13}</td>
                <td>${book.numPages}</td>
                <td>${book.publicationDate != null ? book.publicationDate : 'N/A'}</td>
                <td>${book.publisherId}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>
    <script>
        $(document).ready(function() {
            $('#bookTable').DataTable();
        });
    </script>
</body>
</html>
