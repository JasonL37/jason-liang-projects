from flask import Flask, render_template, request
import psycopg2


# Create a Flask application instance
app = Flask(__name__)
app.secret_key = "123021930148" #secret key 

def get_db_connection():
    connection = None
    try: 
        connection = psycopg2.connect(
            host="localhost",
            database="meal_decider_db",
            user="postgres",
            password="i8@FiRe20$",
            port="5432"
        )
        return connection
    except psycopg2.Error as e:
        print(f"Connecting to the database failed: {e}")
        return None



# Define a route (URL) and the function that handles requests to it
@app.route("/")
def start():
    return render_template("index.html")

@app.route("/results", methods=["GET", "POST"])
def find_recipes():
    conn = None
    try:
        if request.method == "POST":
            ingredients = request.form.get("ingredients", '')
            dietary_restrictions = request.form.getlist("dietary_restrictions")
            spice_tolerance = request.form.get("spice_tolerance")
        
        connection = get_db_connection()
        if connection is None: 
            return "Connection to database failed.", 500
        cursor = connection.cursor()
        cursor.execute("SELECT id, name, instructions, dietary_restrictions, spice_tolerance, ingredients FROM recipes;")

        recipes = cursor.fetchall()
        cursor.close()
        return render_template("results.html", recipes=recipes)
    except psycopg2.DatabaseError as e:
        print(f"Database error: {e}")
        return("An error occurred while attempting to fetch the recipes."), 500
    finally: 
        if connection:
            connection.close()


# Run the Flask development server
if __name__ == '__main__':
    app.run(host='0.0.0.0', debug=True) # debug=True enables debug mode (auto-reloads on changes, shows errors)