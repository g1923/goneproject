# Flask

Anaconda prompt 에서 실행하는 코드 / OS = windows

### 문서

https://flask.palletsprojects.com/en/1.1.x/quickstart/#a-minimal-application

### # 빠른 시작 예시

1. ### Flask 설치하기
```
pip install Flask
```

2. ### Flask 기본 코드 작성하고 app.py 로 파일 저장하기
``` python
from flask import Flask
app = Flask(__name__)

@app.route('/')
def hello_world():
    return 'Hello, World!'
```

3. ### Flask 실행하기

    *** Linux 서버에서는 export -> Windows 에서는 set *** 

- #### 환경변수 설정하기 (OS에 따라 다름)

  (1) 나는 리눅스가 깔려있지 않기때문에 windows에 환경변수를 설정해야한다.
```
set FLASK_APP=app.py
```

  (2) PowerShell에서 실행하는 법
```
$env:FLASK_APP = "hello.py"
```
  (3) python -m 플라스크 <-- 이건 무슨말인지 모르겠다.
```
$ export FLASK_APP=hello.py
$ python -m flask run
 * Running on http://127.0.0.1:5000/
```

- ### 외부에서 접속할 수 있는 서버 설정
디버거를 비활성화했거나 네트워크의 사용자를 신뢰하는 경우 --host=0.0.0.0명령줄에 다음을 추가하여 서버를 공개적으로 사용할 수 있게 만들 수 있습니다.
```
$ flask run --host=0.0.0.0
```

- ### 디버그 모드
```
$ export FLASK_ENV=development
$ flask run
```
4. ### 서버 종료하기
```
ctrl +  c
```