from flask import Flask, request
from flask_script import Manager, Shell
from flask_mail import Mail, Message
from threading import Thread


app = Flask(__name__)
app.config['MAIL_DEBUG'] = True
app.config['MAIL_SUPPRESS_SEND'] = False
app.config['MAIL_SERVER'] = 'smtp.qq.com'
app.config['MAIL_PORT'] = 465
app.config['MAIL_USE_SSL'] = True
app.config['MAIL_USE_TLS'] = False
app.config['MAIL_USERNAME'] = '1316541911@qq.com'
app.config['MAIL_PASSWORD'] = 'sorxwanqvkvjhfgb'
app.config['MAIL_DEFAULT_SENDER'] = '1316541911@qq.com'
manager = Manager(app)
mail = Mail(app)


def send_async_email(app, msg):
    with app.app_context():
        mail.send(msg)


@app.route('/')
def index():
    msg = Message(subject='Hello World',
                  sender="1316541911@qq.com",
                  recipients=['1316541911@qq.com'])
    msg.body = 'sended by flask-email'
    msg.html = '<b>just test<b>'
    thread = Thread(target=send_async_email, args=[app, msg])
    thread.start()
    return 'ok'


if __name__ == '__main__':
    manager.run()
