function validate()
{
    if (document.forms["add-form"]["comment-text-input"].value === "")
    {
        alert("Заполните текст комментария");
        return false;
    }

    return true;
}