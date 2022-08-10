function validate()
{
    if (document.forms["add-form"]["book-name-input"].value === "")
    {
        alert("Заполните наименование книги");
        return false;
    }
    if (document.forms["add-form"]["dropDownListGenre"].value === "0")
    {
        alert("Выберите жанр");
        return false;
    }

    if (document.forms["add-form"]["dropDownListAuthors"].value === "") //Не выбрано ничего
    {
        alert("Выберите авторов");
        return false;
    }

    return true;
}