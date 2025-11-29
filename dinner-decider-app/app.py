from flask import Flask, render_template, request
import psycopg2
import os 

# initialize flask
app = Flask(__name__)
app.secret_key = "123021930148" #secret key 

# connects to database
def get_db_connection():
    connection = None
    try: 
        connection = psycopg2.connect(
            host=os.environ.get("host"),
            database=os.environ.get("database"),
            user=os.environ.get("user"),
            password=os.environ.get("password"),
            port=os.environ.get("port")
        )
        return connection
    except psycopg2.Error as e:
        print(f"Connecting to the database failed: {e}")
        return None

# index. leads to the page with the form to filter the recipes found
@app.route("/")
def start():
    return render_template("index.html")

# page with the recipes based on filter
@app.route("/results", methods=["GET", "POST"])
def find_recipes():
    conn = None
    try:
        if request.method == "POST":
            ingredients = request.form.get("ingredients", "")
            dietary_restrictions = request.form.getlist("dietary-restriction")
            spice_tolerance = request.form.get("spice-level")
        
            connection = get_db_connection()
            if connection is None: 
                return "Connection to database failed.", 500
            cursor = connection.cursor()
            db_query = "SELECT * FROM recipes WHERE 1=1"
            parameters = []
            
            # find recipes that have any ingredients that the user has
            if ingredients:
                ingredient_list = [i.strip() for i in ingredients.split(',') if i.strip()]
                if ingredient_list:
                    ingredient_conditions = []
                    for ingredient in ingredient_list:
                        # filters recipes that have the ingredients that the user entered
                        ingredient_conditions.append("ingredients ILIKE %s")
                        parameters.append(f'%{ingredient}%')
                    
                    # combines all ingredient conditions with OR 
                    db_query += " AND (" + " OR ".join(ingredient_conditions) + ")"
            
            # removes recipes that have a dietary restriction the user selected from the results page
            if dietary_restrictions:
                for restriction in dietary_restrictions:

                    # filters out recipes that have the dietary restriction(s) that the user entered
                    db_query += " AND dietary_restrictions NOT ILIKE %s"
                    parameters.append(f"%{restriction}%")

            # finds the recipe that has the spice tolerance that the user has selected
            if spice_tolerance:

                # filters recipes based on spice tolerance level equal to what the user selected
                db_query += " AND spice_tolerance = %s"
                parameters.append(int(spice_tolerance))
            
            # executes the query that we built from the user inputs
            cursor.execute(db_query, parameters)
            recipes = cursor.fetchall()
            cursor.close()
            return render_template("results.html", recipes=recipes)
        
    except psycopg2.DatabaseError as e:
        print(f"Database error: {e}")
        return("An error occurred while attempting to fetch the recipes."), 500
    
    # makes sure the connection is always closed after use 
    finally: 
        if connection:
            connection.close()


# runs the Flask development server
if __name__ == '__main__':
    app.run(host='0.0.0.0', debug=True) 