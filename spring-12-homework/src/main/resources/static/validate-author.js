function validate()
{
    if (document.forms["add-form"]["author-name-input"].value === "")
    {
        alert("Заполните имя автора");
        return false;
    }

    return true;
}