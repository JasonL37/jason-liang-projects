

// clears the input field when the user clicks on the clear button
document.addEventListener('DOMContentLoaded', function(){
    const clearButton = document.querySelector('.clear');
    const ingredientInput = document.querySelector('#ingredient-search');
    clearButton.addEventListener('click', function(){
        ingredientInput.value = '';
    });
});


// adds the ingredient in the input field based on when the user presses enter
document.addEventListener('DOMContentLoaded', function() {
    const ingredientInput = document.querySelector('#ingredient-search');
    const ingredientListElement = document.querySelector('#ingredient-list');
    const form = document.querySelector('#preference');

    // when the user presses enter in the input field
    ingredientInput.addEventListener('keydown', function(event) {
        if (event.key === 'Enter') {

            // stops the form from submitting  
            event.preventDefault();

            // input from the user minus the white space in the beginning and end
            const newIngredient = ingredientInput.value.trim();

            // we only add the ingredient if it is not an empty string
            if (newIngredient !== "") {

                // creates a new list item for the ingredient
                const newItem = document.createElement("li");

                // sets the inputted ingredient as the text content of the list item
                newItem.textContent = newIngredient;

                // creates a div container for the list item 
                const listItemContainer = document.createElement("div");

                // adds CSS class to the container
                listItemContainer.classList.add("added-ingredient-item");

                // puts the list item inside the div container
                listItemContainer.appendChild(newItem);

                // creates a span element for removing the ingredient from the list
                const listItemRemover = document.createElement("span");

                // text content is empty, we will use CSS to add an "X" icon instead
                listItemRemover.textContent = "";

                // puts the remove button inside the div container
                listItemRemover.classList.add("remove-ingredient");
                listItemContainer.appendChild(listItemRemover);

                // the remove button removes the ingredient from the ingredient list when clicked 
                listItemRemover.addEventListener('click', function() {
                    this.parentNode.remove();
                });
                ingredientListElement.appendChild(listItemContainer);
                ingredientInput.value = '';
            }
        }
    });

    // when the form is submitted, it collects all the ingredients from the list
    form.addEventListener('submit', function(event) {
        const ingredientItems = document.querySelectorAll('#ingredient-list .added-ingredient-item li');
        const listedIngredients = Array.from(ingredientItems).map(item => item.textContent.trim());
        if (listedIngredients.length > 0) {
            ingredientInput.value = listedIngredients.join(', ');
        } else {
            ingredientInput.value = '';
        }
    });
});
