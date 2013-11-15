from bottle import route, run, template

@route('/door/<name>')
def index(name='World'):
    return template('<b>Hello {{name}}</b>!', name=name)

run(host='192.168.0.28', port=8080)
