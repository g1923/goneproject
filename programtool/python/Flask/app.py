# flask 서버를 돌리는 파일의 이름은 통상적으로 app.py

from flask import Flask, url_for,request
from flask import render_template
from markupsafe import escape

app = Flask(__name__)

@app.route('/')
def hello():
    return 'Hello World!'

@app.route('/1')
def test1():
    return '1번 페이지!'

@app.route('/2')
def test2():
    return '2번 페이지'


@app.route('/introduce/<name>')
def introduce(name='홍길동'):
    return render_template('test.html', name=name)

@app.route('/user/<username>')
def profile(username):
    return '{}\'s profile'.format(escape(username))

# 테스트 시에 사용할 코드
with app.test_request_context():
    # 여기에서는 마치 /?name=John 에 대한 요청이 들어온 것처럼 동작함
    print(url_for('hello'))
    print(url_for('test1'))
    print(url_for('test2'))
    print(url_for('introduce', name='John'))  # 변경된 부분
    print(url_for('profile', username='John Doe'))


@app.route('/login', methods=['GET', 'POST'])
def login():
    if request.method == 'POST':
        return do_the_login()
    else:
        return show_the_login_form()


if __name__ == '__main__':  
   app.run(host='0.0.0.0',port=5000,debug=True)