// Keep delete confirmation separate from the page markup.
document.querySelectorAll('.delete-form').forEach(function (form) {
    form.addEventListener('submit', function (event) {
        const title = form.dataset.bookTitle;
        if (!window.confirm('Delete "' + title + '" from your collection? This cannot be undone.')) {
            event.preventDefault();
        }
    });
});
