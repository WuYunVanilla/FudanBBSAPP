from db import Session, User


class UserOp():
    @staticmethod
    def insert(stdId, password, valid):
        sess = Session()
        new_user = User(stdId=stdId, password=password, valid=valid,
                        nickname=stdId, gender="Male", grade="Freshman", signature="", photo="",
                        posting="", answer="", follow="", collection="", view="")
        sess.add(new_user)
        sess.commit()

    @staticmethod
    def query(stdId):
        sess = Session()
        q_usr = sess.query(User).filter_by(stdId=stdId).first()
        # q_usr.view = "1\t2"
        sess.commit()
        return q_usr


    @staticmethod
    def modifyPassword(stdId, password):
        sess = Session()
        q_usr = sess.query(User).filter_by(stdId=stdId).first()
        q_usr.password = password
        sess.commit()

    @staticmethod
    def confirm(stdId):
        sess = Session()
        q_usr = sess.query(User).filter_by(stdId=stdId).first()
        q_usr.valid = True
        sess.commit()

    @staticmethod
    def printf():
        sess = Session()
        for instance in sess.query(User).order_by(User.id):
            print instance
        sess.commit()

    @staticmethod
    def modify(stdId, key, value):
        sess = Session()
        q_usr = sess.query(User).filter_by(stdId=stdId).first()
        if key == "nickname":
            q_usr.nickname = value
        elif key == "gender":
            q_usr.gender = value
        elif key == "grade":
            q_usr.grade = value
        elif key == "signature":
            q_usr.signature = value
        elif key == "photo":
            q_usr.photo = value
        elif key == "password":
            q_usr.password = value
        sess.commit()

    @staticmethod
    def add_posting(stdId, questionId):
        sess = Session()
        q_usr = sess.query(User).filter_by(stdId=stdId).first()
        if q_usr.posting == "":
            q_usr.posting = str(questionId)
        else:
            split = q_usr.posting.split("\t")
            if str(questionId) not in split:
                q_usr.posting += ("\t" + str(questionId))
        sess.commit()

    @staticmethod
    def add_answer(stdId, answerId):
        sess = Session()
        q_usr = sess.query(User).filter_by(stdId=stdId).first()
        if q_usr.answer == "":
            q_usr.answer = str(answerId)
        else:
            split = q_usr.answer.split("\t")
            if str(answerId) not in split:
                q_usr.answer += ("\t" + str(answerId))
        sess.commit()

    @staticmethod
    def add_follow(stdId, questionId):
        sess = Session()
        q_usr = sess.query(User).filter_by(stdId=stdId).first()
        if q_usr.follow == "":
            q_usr.follow = str(questionId)
        else:
            split = q_usr.follow.split("\t")
            if str(questionId) not in split:
                q_usr.follow += ("\t" + str(questionId))
        sess.commit()

    @staticmethod
    def add_collection(stdId, answerId):
        sess = Session()
        q_usr = sess.query(User).filter_by(stdId=stdId).first()
        if q_usr.collection == "":
            q_usr.collection = str(answerId)
        else:
            split = q_usr.collection.split("\t")
            if str(answerId) not in split:
                q_usr.collection += ("\t" + str(answerId))
        sess.commit()

    @staticmethod
    def add_view(stdId, questionId):
        sess = Session()
        q_usr = sess.query(User).filter_by(stdId=stdId).first()
        if q_usr.view == "":
            q_usr.view = str(questionId)
        else:
            split = q_usr.view.split("\t")
            if str(questionId) not in split:
                q_usr.view += ("\t" + str(questionId))
        sess.commit()
