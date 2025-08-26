let ingredientList = [];

document.addEventListener('DOMContentLoaded', function(){
    const clearButton = document.querySelector('.clear');
    const ingredientInput = document.getElementById('ingredient-search');

    clearButton.addEventListener('click', function(){
        ingredientInput.value = '';
    });
});

document.addEventListener('DOMContentLoaded', function() {
    const ingredientInput = document.getElementById('ingredient-search');
    const ingredientListElement = document.getElementById('ingredient-list');

    ingredientInput.addEventListener('keydown', function(event) {
        if (event.key === 'Enter') {
            event.preventDefault();

            const newIngredient = ingredientInput.value.trim();

            if (newIngredient !== "") {
                const newItem = document.createElement("li");
                newItem.textContent = newIngredient;

                const listItemContainer = document.createElement("div");
                listItemContainer.classList.add("added-ingredient-item");
                listItemContainer.appendChild(newItem);

                const listItemRemover = document.createElement("span");
                listItemRemover.textContent = "";
                listItemRemover.classList.add("remove-ingredient");
                listItemContainer.appendChild(listItemRemover);

                // This is the key part that you need to add 👇
                listItemRemover.addEventListener('click', function() {
                  // The `this` keyword refers to the clicked removeButton.
                  // We remove its parent, which is the whole ingredient container.
                    this.parentNode.remove();
                });
                ingredientListElement.appendChild(listItemContainer);
                ingredientInput.value = '';
            }
        }
    });
});