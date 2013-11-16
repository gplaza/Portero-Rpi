from bottle import route, run, template, request

@route('/door/<name>')
def index(name='World'):
    return template('<b>Hello {{name}}</b>!', name=name)

@route('/login', method='POST')
def login():
    username = request.POST.get('user', '')
    password = request.POST.get('password', '')
    if username == 'javi' and password == 'test':
        return { "success" : True }
    else:
        return { "success" : False }

run(host='192.168.1.210', port=7000)
