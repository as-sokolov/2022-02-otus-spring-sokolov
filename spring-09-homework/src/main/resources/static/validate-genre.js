function validate()
{
    if (document.forms["add-form"]["genre-name-input"].value === "")
    {
        alert("Заполните наименование жанра");
        return false;
    }

    return true;
}