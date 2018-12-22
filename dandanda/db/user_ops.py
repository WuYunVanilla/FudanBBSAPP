from db import Session, User


class UserOp():
    @staticmethod
    def insert(stdId, password, valid):
        sess = Session()
        new_user = User(stdId=stdId, password=password, valid=valid)
        sess.add(new_user)
        sess.commit()

    @staticmethod
    def query(stdId):
        sess = Session()
        q_usr = sess.query(User).filter_by(stdId=stdId).first()
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
